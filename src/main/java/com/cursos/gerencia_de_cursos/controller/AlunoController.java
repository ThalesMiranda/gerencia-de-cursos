package com.cursos.gerencia_de_cursos.controller;

import com.cursos.gerencia_de_cursos.model.Aluno;
import com.cursos.gerencia_de_cursos.repository.AlunoRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller REST para gerenciar a entidade Aluno (CRUD).
 * Mapeia as operações HTTP para a persistência via AlunoRepository.
 */
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    /**
     * POST /api/alunos: Cria um novo Aluno.
     * Inclui a validação de unicidade de CPF.
     * @param novoAluno Os dados do aluno a ser criado.
     * @return 201 Created com o aluno persistido.
     */
    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@Valid @RequestBody Aluno novoAluno) {
        
        // 1. Validação de Unicidade de CPF
        if (alunoRepository.findByCpf(novoAluno.getCpf()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, // 409 Conflict
                "CPF já cadastrado no sistema."
            );
        }
        
        // 2. Salva o Aluno
        Aluno alunoSalvo = alunoRepository.save(novoAluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoSalvo);
    }

    /**
     * GET /api/alunos: Lista todos os Alunos.
     * @return 200 OK com a lista de alunos.
     */
    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        List<Aluno> alunos = StreamSupport.stream(alunoRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }

    /**
     * GET /api/alunos/{id}: Busca um Aluno pelo ID.
     * @param id O ID do aluno.
     * @return 200 OK com o aluno, ou 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return alunoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado com ID: " + id));
    }

    /**
     * PUT /api/alunos/{id}: Atualiza um Aluno existente.
     * @param id O ID do aluno a ser atualizado.
     * @param dadosAluno Os novos dados do aluno.
     * @return 200 OK com o aluno atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @Valid @RequestBody Aluno dadosAluno) {
        return alunoRepository.findById(id).map(alunoExistente -> {
            
            // 1. O CPF não deve ser alterado para um que já exista em outro aluno.
            if (!alunoExistente.getCpf().equals(dadosAluno.getCpf())) {
                if (alunoRepository.findByCpf(dadosAluno.getCpf()).isPresent()) {
                    throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Não é possível alterar o CPF para um valor já cadastrado."
                    );
                }
                alunoExistente.setCpf(dadosAluno.getCpf());
            }

            // 2. Atualiza os demais campos
            alunoExistente.setNome(dadosAluno.getNome());
            alunoExistente.setEmail(dadosAluno.getEmail());
            
            // 3. Salva e retorna
            Aluno alunoAtualizado = alunoRepository.save(alunoExistente);
            return ResponseEntity.ok(alunoAtualizado);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado para atualização com ID: " + id));
    }

    /**
     * DELETE /api/alunos/{id}: Exclui um Aluno pelo ID.
     * @param id O ID do aluno a ser excluído.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content
    public void deletarAluno(@PathVariable Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado para exclusão com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
}