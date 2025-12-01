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

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * GET /api/professores - Lista todos (CR3 - Read/Index)
     */
    @GetMapping
    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    /**
     * GET /api/professores/{id} - Busca por ID (CR3 - Read/Show)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Professor não encontrado com ID: " + id
                ));
        return ResponseEntity.ok(professor);
    }

    /**
     * POST /api/professores - Cria um novo Professor (CR3 - Create/Store)
     */
    @PostMapping
    public ResponseEntity<Professor> criar(@Valid @RequestBody Professor professor) {
        // O JPA garante a unicidade do email (caso duplicado, lança exceção).
        Professor novoProfessor = professorRepository.save(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProfessor);
    }

    /**
     * PUT /api/professores/{id} - Atualiza um Professor (CR3 - Update)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @Valid @RequestBody Professor dadosProfessor) {
        Professor professorExistente = professorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Professor não encontrado para atualização com ID: " + id
                ));

        // Atualiza os campos
        professorExistente.setNome(dadosProfessor.getNome());
        professorExistente.setEmail(dadosProfessor.getEmail());
        professorExistente.setAreaEspecializacao(dadosProfessor.getAreaEspecializacao());

        Professor professorAtualizado = professorRepository.save(professorExistente);
        return ResponseEntity.ok(professorAtualizado);
    }

    /**
     * DELETE /api/professores/{id} - Exclui um Professor (CR3 - Delete/Destroy)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Professor não encontrado para exclusão com ID: " + id
            );
        }
        professorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}