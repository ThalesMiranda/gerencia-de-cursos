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

/**
 * Controller REST para gerenciar a entidade Curso (CRUD).
 * Mapeia as operações HTTP para a persistência via CursoRepository.
 */
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    /** * Injeção de Dependência do Repositório (Slide 25). 
     * Permite o acesso aos métodos CRUD do Spring Data JPA.
     */
    @Autowired
    private CursoRepository cursoRepository;

    /**
     * POST /cursos: Cria um novo Curso.
     * @param novoCurso Os dados do curso a ser criado.
     * @return 201 Created com o curso persistido.
     */
    @PostMapping
    public ResponseEntity<Curso> criarCurso(@Valid @RequestBody Curso novoCurso) {
        // O @Valid garante que as anotações como @NotBlank e @Min sejam verificadas.
        Curso cursoSalvo = cursoRepository.save(novoCurso);
        // Retorna o status 201 Created (sucesso)
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
    }

    /**
     * GET /cursos: Lista todos os Cursos.
     * @return 200 OK com a lista de cursos.
     */
    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        // findAll() retorna um Iterable. Convertemos para List para o retorno JSON.
        List<Curso> cursos = StreamSupport.stream(cursoRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(cursos); // Retorna o status 200 OK
    }

    /**
     * GET /cursos/{id}: Busca um Curso pelo ID.
     * @param id O ID do curso.
     * @return 200 OK com o curso, ou 404 Not Found se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        
        // Se o curso existir, retorna 200 OK.
        // Caso contrário, lança uma exceção que o Spring converte para 404 Not Found.
        return curso.map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado com ID: " + id));
    }

    /**
     * PUT /cursos/{id}: Atualiza um Curso existente.
     * @param id O ID do curso a ser atualizado.
     * @param dadosCurso Os novos dados do curso.
     * @return 200 OK com o curso atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizarCurso(@PathVariable Long id, @Valid @RequestBody Curso dadosCurso) {
        return cursoRepository.findById(id).map(cursoExistente -> {
            
            // Atualiza os campos
            cursoExistente.setNome(dadosCurso.getNome());
            cursoExistente.setDescricao(dadosCurso.getDescricao());
            cursoExistente.setCargaHoraria(dadosCurso.getCargaHoraria());
            
            // Salva e retorna o curso atualizado
            Curso cursoAtualizado = cursoRepository.save(cursoExistente);
            return ResponseEntity.ok(cursoAtualizado);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado para atualização com ID: " + id));
    }

    /**
     * DELETE /cursos/{id}: Exclui um Curso pelo ID.
     * @param id O ID do curso a ser excluído.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content
    public void deletarCurso(@PathVariable Long id) {
        // Verifica se o curso existe antes de deletar, para retornar 404 se não existir
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado para exclusão com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}