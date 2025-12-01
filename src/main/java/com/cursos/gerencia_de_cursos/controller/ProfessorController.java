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

/**
 * Controller REST para gerenciar a entidade Professor (CRUD).
 * Mapeia as operações HTTP para a persistência via ProfessorRepository.
 */
@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * POST /api/professores: Cria um novo Professor.
     * @param novoProfessor Os dados do professor a ser criado.
     * @return 201 Created com o professor persistido.
     */
    @PostMapping
    public ResponseEntity<Professor> criarProfessor(@Valid @RequestBody Professor novoProfessor) {
        // O @Valid verifica as restrições de @NotBlank, @Size, etc.
        Professor professorSalvo = professorRepository.save(novoProfessor);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }

    /**
     * GET /api/professores: Lista todos os Professores.
     * @return 200 OK com a lista de professores.
     */
    @GetMapping
    public ResponseEntity<List<Professor>> listarTodos() {
        List<Professor> professores = StreamSupport.stream(professorRepository.findAll().spliterator(), false)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(professores);
    }

    /**
     * GET /api/professores/{id}: Busca um Professor pelo ID.
     * @param id O ID do professor.
     * @return 200 OK com o professor, ou 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
        return professorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado com ID: " + id));
    }

    /**
     * PUT /api/professores/{id}: Atualiza um Professor existente.
     * @param id O ID do professor a ser atualizado.
     * @param dadosProfessor Os novos dados do professor.
     * @return 200 OK com o professor atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizarProfessor(@PathVariable Long id, @Valid @RequestBody Professor dadosProfessor) {
        return professorRepository.findById(id).map(professorExistente -> {
            
            // Atualiza os campos
            professorExistente.setNome(dadosProfessor.getNome());
            professorExistente.setAreaEspecializacao(dadosProfessor.getAreaEspecializacao());
            professorExistente.setCurriculo(dadosProfessor.getCurriculo());
            
            // Salva e retorna
            Professor professorAtualizado = professorRepository.save(professorExistente);
            return ResponseEntity.ok(professorAtualizado);
            
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado para atualização com ID: " + id));
    }

    /**
     * DELETE /api/professores/{id}: Exclui um Professor pelo ID.
     * @param id O ID do professor a ser excluído.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content
    public void deletarProfessor(@PathVariable Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado para exclusão com ID: " + id);
        }
        professorRepository.deleteById(id);
    }
}