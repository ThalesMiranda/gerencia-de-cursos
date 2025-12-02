package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Professor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, Long> {
    
}