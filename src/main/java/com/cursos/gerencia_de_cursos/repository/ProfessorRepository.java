package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Professor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Professor.
 * Estende CrudRepository para obter os métodos CRUD automáticos.
 */
@Repository
public interface ProfessorRepository extends CrudRepository<Professor, Long> {
    
    // O Spring Data JPA cria os métodos CRUD automaticamente.
}