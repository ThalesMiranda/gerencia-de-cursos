package com.cursos.gerencia_de_cursos.controller;


import com.cursos.gerencia_de_cursos.model.Turma;
import com.cursos.gerencia_de_cursos.repository.TurmaRepository;
import com.cursos.gerencia_de_cursos.repository.CursoRepository;
import com.cursos.gerencia_de_cursos.repository.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional; // Adicionar este import

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;
    
    // Repositories necessários para buscar FKs
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * GET /api/turmas - Lista todas as Turmas, carregando Curso e Professor (CR5 - Read/Index)
     */
    @GetMapping
    @Transactional // Garante que a sessão Hibernate fique aberta durante a serialização
    public List<Turma> listarTodos() {
        return turmaRepository.findAll();
    }

    /**
     * GET /api/turmas/{id} - Busca Turma por ID, carregando Alunos (CR5 - Read/Show)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada com ID: " + id
                ));
        return ResponseEntity.ok(turma);
    }

    /**
     * POST /api/turmas - Cria uma nova Turma (CR5 - Create/Store)
     * O corpo JSON deve conter apenas os IDs de curso e professor:
     * Ex: { "codigo": "...", "dataInicio": "...", "dataFim": "...", "curso": {"id": 1}, "professor": {"id": 1} }
     */
    @PostMapping
    public ResponseEntity<Turma> criar(@Valid @RequestBody Turma turma) {
        
        // As entidades Curso e Professor são automaticamente resolvidas pelo Hibernate, 
        // desde que o JSON contenha apenas o ID dentro do objeto aninhado.
        
        // Verificação básica de existência das FKs para um bom feedback ao usuário
        if (!cursoRepository.existsById(turma.getCurso().getId())) {
             throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Curso não encontrado com ID: " + turma.getCurso().getId()
            );
        }
        if (turma.getProfessor() != null && turma.getProfessor().getId() != null && !professorRepository.existsById(turma.getProfessor().getId())) {
             throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Professor não encontrado com ID: " + turma.getProfessor().getId()
            );
        }
        Turma novaTurma = turmaRepository.save(turma);
        // 1. Recarrega a turma para garantir que os objetos Curso e Professor não sejam proxies preguiçosos
        Turma turmaCarregada = turmaRepository.findById(novaTurma.getId()).orElse(novaTurma);

        // 2. Retorna 201 Created com a turma carregada
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaCarregada);
    }

    /**
     * PUT /api/turmas/{id} - Atualiza uma Turma (CR5 - Update)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizar(@PathVariable Long id, @Valid @RequestBody Turma dadosTurma) {
        Turma turmaExistente = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada para atualização com ID: " + id
                ));

        // Atualiza campos básicos
        turmaExistente.setCodigo(dadosTurma.getCodigo());
        turmaExistente.setDataInicio(dadosTurma.getDataInicio());
        turmaExistente.setDataFim(dadosTurma.getDataFim());

        // Atualiza FKs (Curso e Professor)
        if (dadosTurma.getCurso() != null) {
            turmaExistente.setCurso(dadosTurma.getCurso());
        }
        if (dadosTurma.getProfessor() != null) {
             turmaExistente.setProfessor(dadosTurma.getProfessor());
        }
        
        Turma turmaAtualizada = turmaRepository.save(turmaExistente);
        return ResponseEntity.ok(turmaAtualizada);
    }

    /**
     * DELETE /api/turmas/{id} - Exclui uma Turma (CR5 - Delete/Destroy)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Turma não encontrada para exclusão com ID: " + id
            );
        }
        
        // A remoção de Turma aciona a exclusão em cascata (ou set null) nas matrículas (aluno_turma)
        turmaRepository.deleteById(id); 
        return ResponseEntity.noContent().build();
    }
}