package com.cursos.gerencia_de_cursos.repository;


import com.cursos.gerencia_de_cursos.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    // Nenhuma query personalizada é necessária para o CRUD básico.
}