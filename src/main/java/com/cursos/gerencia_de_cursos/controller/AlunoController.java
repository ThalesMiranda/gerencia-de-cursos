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

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@Valid @RequestBody Aluno novoAluno) {
        
        if (alunoRepository.findByCpf(novoAluno.getCpf()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "CPF já cadastrado no sistema."
            );
        }
        
        Aluno alunoSalvo = alunoRepository.save(novoAluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        List<Aluno> alunos = StreamSupport.stream(alunoRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return alunoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado com ID: " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @Valid @RequestBody Aluno dadosAluno) {
        return alunoRepository.findById(id).map(alunoExistente -> {
            
            if (!alunoExistente.getCpf().equals(dadosAluno.getCpf())) {
                if (alunoRepository.findByCpf(dadosAluno.getCpf()).isPresent()) {
                    throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Não é possível alterar o CPF para um valor já cadastrado."
                    );
                }
                alunoExistente.setCpf(dadosAluno.getCpf());
            }

            alunoExistente.setNome(dadosAluno.getNome());
            alunoExistente.setEmail(dadosAluno.getEmail());
            
            Aluno alunoAtualizado = alunoRepository.save(alunoExistente);
            return ResponseEntity.ok(alunoAtualizado);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado para atualização com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarAluno(@PathVariable Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado para exclusão com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
}