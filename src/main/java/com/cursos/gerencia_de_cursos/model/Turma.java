package com.cursos.gerencia_de_cursos.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa uma instância específica de um Curso (Turma). 
 * É a entidade central que gerencia os relacionamentos do modelo.
 * * Usamos @JsonIdentityInfo (Slide 57) para evitar loops infinitos 
 * de serialização JSON, especialmente na relação N:M com Aluno.
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@JsonIgnoreProperties("alunos")

public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O código da turma é obrigatório.")
    @Column(unique = true)
    private String codigoTurma;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória.")
    private LocalDate dataFim;

    // --- RELACIONAMENTOS 1:N / Muitos-para-Um (ManyToOne) ---
    
    /**
     * Relacionamento N:1 com Curso (Um Curso pode ter VÁRIAS Turmas).
     * Turma é o lado N (Subordinada). Curso é o lado 1 (Dominante).
     * A Turma possui a chave estrangeira (fk_curso_id).
     */
    @ManyToOne(fetch = FetchType.EAGER) // Geralmente carregado de forma EAGER
    @JoinColumn(name = "curso_id", nullable = false)
    @NotNull(message = "A turma deve estar associada a um Curso.")
    private Curso curso;

    /**
     * Relacionamento N:1 com Professor (Um Professor pode ministrar VÁRIAS Turmas).
     * A Turma possui a chave estrangeira (fk_professor_id).
     */
    @ManyToOne(fetch = FetchType.EAGER) // Geralmente carregado de forma EAGER
    @JoinColumn(name = "professor_id", nullable = false)
    @NotNull(message = "A turma deve ter um Professor responsável.")
    private Professor professor;

    // --- RELACIONAMENTO N:M / Muitos-para-Muitos (ManyToMany) ---

    /**
     * Relacionamento N:M com Aluno (Uma Turma tem VÁRIOS Alunos, e um Aluno pode 
     * estar em VÁRIAS Turmas).
     * * O @JoinTable configura a tabela intermediária 'turma_aluno'.
     * 'joinColumns' é a chave da Turma (fk_turma_id) na tabela intermediária.
     * 'inverseJoinColumns' é a chave do Aluno (fk_aluno_id) na tabela intermediária.
     * (Conforme Slide 50)
     */
    @ManyToMany(fetch = FetchType.LAZY) // Usamos LAZY para não sobrecarregar
    @JoinTable(
        name = "turma_aluno", 
        joinColumns = @JoinColumn(name = "turma_id"),
        inverseJoinColumns = @JoinColumn(name = "aluno_id"))
    private Set<Aluno> alunos = new HashSet<>();

    // O Lombok gera os getters e setters, que são essenciais para o Hibernate/JPA.
}