package com.cursos.gerencia_de_cursos.controller;

import com.cursos.gerencia_de_cursos.model.Aluno;
import com.cursos.gerencia_de_cursos.model.Turma;
import com.cursos.gerencia_de_cursos.model.Curso; 
import com.cursos.gerencia_de_cursos.model.Professor; 
import com.cursos.gerencia_de_cursos.repository.AlunoRepository;
import com.cursos.gerencia_de_cursos.repository.TurmaRepository;
import com.cursos.gerencia_de_cursos.repository.CursoRepository;
import com.cursos.gerencia_de_cursos.repository.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.transaction.annotation.Transactional; 

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private CursoRepository cursoRepository; 
    @Autowired
    private ProfessorRepository professorRepository; 
    @Autowired
    private AlunoRepository alunoRepository; 


    @PostMapping
    public ResponseEntity<Turma> criarTurma(@Valid @RequestBody Turma novaTurma) {
        
        if (turmaRepository.findByCodigoTurma(novaTurma.getCodigoTurma()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Código da turma já cadastrado.");
        }
        
        Curso cursoReferencia = cursoRepository.findById(novaTurma.getCurso().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso não encontrado para associação."));
        novaTurma.setCurso(cursoReferencia); 
        
        Professor professorReferencia = professorRepository.findById(novaTurma.getProfessor().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Professor não encontrado para associação."));
        novaTurma.setProfessor(professorReferencia); 

        Turma turmaSalva = turmaRepository.save(novaTurma);
        
        Turma turmaCompleta = turmaRepository.findById(turmaSalva.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recarregar a turma salva."));
        
        
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaCompleta);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<Turma>> listarTodos() {
        List<Turma> turmas = StreamSupport.stream(turmaRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        
        turmas.forEach(t -> t.getAlunos().size()); 

        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/{id}")
    @Transactional 
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada com ID: " + id));

        turma.getAlunos().size();
        
        return ResponseEntity.ok(turma);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable Long id, @Valid @RequestBody Turma dadosTurma) {
        return turmaRepository.findById(id).map(turmaExistente -> {
            
            Curso cursoReferencia = cursoRepository.findById(dadosTurma.getCurso().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso não encontrado para associação."));
            
            Professor professorReferencia = professorRepository.findById(dadosTurma.getProfessor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Professor não encontrado para associação."));

            turmaExistente.setCodigoTurma(dadosTurma.getCodigoTurma());
            turmaExistente.setDataInicio(dadosTurma.getDataInicio());
            turmaExistente.setDataFim(dadosTurma.getDataFim());
            
            turmaExistente.setCurso(cursoReferencia); 
            turmaExistente.setProfessor(professorReferencia); 
            
            Turma turmaAtualizada = turmaRepository.save(turmaExistente);

            Turma turmaCompleta = turmaRepository.findById(turmaAtualizada.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recarregar a turma atualizada."));

            return ResponseEntity.ok(turmaCompleta);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada para atualização com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void deletarTurma(@PathVariable Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada para exclusão com ID: " + id);
        }
        turmaRepository.deleteById(id);
    }
    

    @PostMapping("/{turmaId}/matricular/{alunoId}")
    @Transactional
    public ResponseEntity<Turma> matricularAluno(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        
        Turma turma = turmaRepository.findById(turmaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada."));
            
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        if (turma.getAlunos().contains(aluno)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aluno já matriculado nesta turma.");
        }
            
        turma.getAlunos().add(aluno);
        Turma turmaAtualizada = turmaRepository.save(turma); 
        
        Turma turmaCompleta = turmaRepository.findById(turmaAtualizada.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recarregar a turma salva após matrícula."));
        
        turmaCompleta.getAlunos().size(); 

        return ResponseEntity.ok(turmaCompleta);
    }

    @DeleteMapping("/{turmaId}/desmatricular/{alunoId}")
    @Transactional
    public ResponseEntity<Turma> desmatricularAluno(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        
        Turma turma = turmaRepository.findById(turmaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada."));
            
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        if (!turma.getAlunos().contains(aluno)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não está matriculado nesta turma.");
        }
            
        turma.getAlunos().remove(aluno);
        Turma turmaAtualizada = turmaRepository.save(turma); 
        
        Turma turmaCompleta = turmaRepository.findById(turmaAtualizada.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recarregar a turma atualizada após desmatrícula."));

        turmaCompleta.getAlunos().size();

        return ResponseEntity.ok(turmaCompleta);
    }
}