package com.cursos.gerencia_de_cursos.controller;

import com.cursos.gerencia_de_cursos.model.Professor;
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

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<Professor> criarProfessor(@Valid @RequestBody Professor novoProfessor) {
        Professor professorSalvo = professorRepository.save(novoProfessor);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Professor>> listarTodos() {
        List<Professor> professores = StreamSupport.stream(professorRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
        return professorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado com ID: " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizarProfessor(@PathVariable Long id, @Valid @RequestBody Professor dadosProfessor) {
        return professorRepository.findById(id).map(professorExistente -> {
            
            professorExistente.setNome(dadosProfessor.getNome());
            professorExistente.setAreaEspecializacao(dadosProfessor.getAreaEspecializacao());
            professorExistente.setCurriculo(dadosProfessor.getCurriculo());
            
            Professor professorAtualizado = professorRepository.save(professorExistente);
            return ResponseEntity.ok(professorAtualizado);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado para atualização com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProfessor(@PathVariable Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado para exclusão com ID: " + id);
        }
        professorRepository.deleteById(id);
    }
}