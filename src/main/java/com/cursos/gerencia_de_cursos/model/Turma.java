package com.cursos.gerencia_de_cursos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @NotNull
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    // --- Relacionamentos 1:N (CR6) ---
    
    // N:1 com Curso. Uma Turma pertence a UM Curso.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false) // Coluna FK na tabela 'turmas'
    @NotNull
    private Curso curso;

    // N:1 com Professor. Uma Turma tem UM Professor.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id") // Coluna FK na tabela 'turmas'. Nullable no DB.
    private Professor professor;

    // --- Relacionamento N:M (CR7) ---
    
    // Muitos-para-Muitos com Aluno.
    // Usamos JoinTable para especificar o nome da tabela intermediária (aluno_turma)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "aluno_turma", // Nome da tabela pivot no banco
        joinColumns = @JoinColumn(name = "turma_id"),
        inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private Set<Aluno> alunos = new HashSet<>();
}