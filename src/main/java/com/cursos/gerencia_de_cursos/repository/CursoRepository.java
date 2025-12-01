package com.cursos.gerencia_de_cursos.repository;

import com.cursos.gerencia_de_cursos.model.Curso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Curso.
 * Estendendo CrudRepository, o Spring Data JPA cria automaticamente
 * todos os métodos CRUD (save, findById, findAll, delete, etc.)
 * para a entidade Curso com chave primária do tipo Long.
 */
@Repository
public interface CursoRepository extends CrudRepository<Curso, Long> {
    
    // Nenhuma implementação de método é necessária aqui para o CRUD básico.
    // O Spring Data JPA faz o trabalho.
}