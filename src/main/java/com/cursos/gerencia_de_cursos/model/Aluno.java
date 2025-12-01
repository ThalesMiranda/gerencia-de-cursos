package com.cursos.gerencia_de_cursos.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @NotBlank
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^[0-9]{11}$", message = "O CPF deve conter exatamente 11 dígitos.")
    // Importante: unique = true garante que o CPF seja único
    @Column(nullable = false, unique = true, length = 11) 
    private String cpf;

    @Email
    @NotBlank
    @Size(max = 150)
    // Importante: unique = true garante que o email seja único
    @Column(nullable = false, unique = true, length = 150) 
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    // Relacionamento Muitos-para-Muitos com Turma (Matrícula) será adicionado depois
}