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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<Curso> criarCurso(@Valid @RequestBody Curso novoCurso) {
        Curso cursoSalvo = cursoRepository.save(novoCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        List<Curso> cursos = StreamSupport.stream(cursoRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        
        return curso.map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado com ID: " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizarCurso(@PathVariable Long id, @Valid @RequestBody Curso dadosCurso) {
        return cursoRepository.findById(id).map(cursoExistente -> {
            
            cursoExistente.setNome(dadosCurso.getNome());
            cursoExistente.setDescricao(dadosCurso.getDescricao());
            cursoExistente.setCargaHoraria(dadosCurso.getCargaHoraria());
            
            Curso cursoAtualizado = cursoRepository.save(cursoExistente);
            return ResponseEntity.ok(cursoAtualizado);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado para atualização com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCurso(@PathVariable Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado para exclusão com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}