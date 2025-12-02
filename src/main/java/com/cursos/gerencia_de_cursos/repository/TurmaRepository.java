package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Turma;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade Turma.
 */
@Repository
public interface TurmaRepository extends CrudRepository<Turma, Long> {
    
    /**
     * Busca uma Turma pelo seu código (que deve ser único).
     */
    Optional<Turma> findByCodigoTurma(String codigoTurma);
}