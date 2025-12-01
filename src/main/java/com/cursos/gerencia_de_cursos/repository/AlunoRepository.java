package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade Aluno.
 */
@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {
    
    /**
     * Derived Query do Spring Data JPA.
     * Cria automaticamente a query SQL para buscar um Aluno pelo seu CPF.
     * Essencial para verificar a unicidade antes de salvar.
     * @param cpf O CPF do aluno a ser buscado.
     * @return Um Optional contendo o Aluno, se encontrado.
     */
    Optional<Aluno> findByCpf(String cpf);
}