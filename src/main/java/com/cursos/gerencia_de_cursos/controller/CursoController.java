package com.cursos.gerencia_de_cursos.controller;

import com.cursos.gerencia_de_cursos.model.Curso;
import com.cursos.gerencia_de_cursos.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * GET /api/cursos - Lista todos os Cursos (CR2 - Read/Index)
     */
    @GetMapping
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    /**
     * GET /api/cursos/{id} - Busca um Curso por ID (CR2 - Read/Show)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado com ID: " + id
                ));
        // Retorna 200 OK
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos - Cria um novo Curso (CR2 - Create/Store)
     */
    @PostMapping
    // @Valid ativa a validação (usaremos nas entidades para @NotNull, etc.)
    public ResponseEntity<Curso> criar(@Valid @RequestBody Curso curso) {
        // O Hibernate salvará automaticamente os dados
        Curso novoCurso = cursoRepository.save(curso);
        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso);
    }

    /**
     * PUT/PATCH /api/cursos/{id} - Atualiza um Curso (CR2 - Update)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @Valid @RequestBody Curso dadosCurso) {
        // 1. Busca o curso existente
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado para atualização com ID: " + id
                ));

        // 2. Atualiza os campos
        cursoExistente.setNome(dadosCurso.getNome());
        cursoExistente.setDescricao(dadosCurso.getDescricao());
        cursoExistente.setCargaHoraria(dadosCurso.getCargaHoraria());

        // 3. Salva e retorna 200 OK
        Curso cursoAtualizado = cursoRepository.save(cursoExistente);
        return ResponseEntity.ok(cursoAtualizado);
    }

    /**
     * DELETE /api/cursos/{id} - Exclui um Curso (CR2 - Delete/Destroy)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Curso não encontrado para exclusão com ID: " + id
            );
        }
        cursoRepository.deleteById(id);
        // Retorna 204 No Content (padrão REST para exclusão bem-sucedida)
        return ResponseEntity.noContent().build();
    }
}