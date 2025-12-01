package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
}