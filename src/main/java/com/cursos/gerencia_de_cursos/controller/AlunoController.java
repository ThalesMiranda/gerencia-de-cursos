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

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    /**
     * GET /api/alunos - Lista todos (CR4 - Read/Index)
     */
    @GetMapping
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    /**
     * GET /api/alunos/{id} - Busca por ID (CR4 - Read/Show)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com ID: " + id
                ));
        return ResponseEntity.ok(aluno);
    }

    /**
     * POST /api/alunos - Cria um novo Aluno (CR4 - Create/Store)
     */
    @PostMapping
    public ResponseEntity<Aluno> criar(@Valid @RequestBody Aluno aluno) {
        Aluno novoAluno = alunoRepository.save(aluno);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
    }

    /**
     * PUT /api/alunos/{id} - Atualiza um Aluno (CR4 - Update)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @Valid @RequestBody Aluno dadosAluno) {
        Aluno alunoExistente = alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado para atualização com ID: " + id
                ));

        // Atualiza os campos
        alunoExistente.setNome(dadosAluno.getNome());
        alunoExistente.setCpf(dadosAluno.getCpf());
        alunoExistente.setEmail(dadosAluno.getEmail());
        alunoExistente.setDataNascimento(dadosAluno.getDataNascimento());

        Aluno alunoAtualizado = alunoRepository.save(alunoExistente);
        return ResponseEntity.ok(alunoAtualizado);
    }

    /**
     * DELETE /api/alunos/{id} - Exclui um Aluno (CR4 - Delete/Destroy)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado para exclusão com ID: " + id
            );
        }
        alunoRepository.deleteById(id);
        // Retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}
