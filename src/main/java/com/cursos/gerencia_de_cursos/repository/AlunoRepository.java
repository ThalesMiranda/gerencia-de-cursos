package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {
    
    Optional<Aluno> findByCpf(String cpf);
}