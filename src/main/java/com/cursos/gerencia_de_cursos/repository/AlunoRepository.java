package com.cursos.gerencia_de_cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cursos.gerencia_de_cursos.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    // Nenhuma query personalizada é necessária para o CRUD básico.
}
