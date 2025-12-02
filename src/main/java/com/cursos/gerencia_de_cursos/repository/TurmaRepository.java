package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Turma;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaRepository extends CrudRepository<Turma, Long> {
    
    Optional<Turma> findByCodigoTurma(String codigoTurma);
}