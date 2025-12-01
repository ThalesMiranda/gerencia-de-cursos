package com.cursos.gerencia_de_cursos.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professors")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @Email
    @NotBlank
    @Size(max = 150)
    // Importante: unique = true garante que o email seja único no banco
    @Column(nullable = false, unique = true, length = 150) 
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(name = "area_especializacao", nullable = false, length = 100)
    private String areaEspecializacao;

    // Relacionamento Um-para-Muitos com Turma será adicionado depois
}