package com.cursos.gerencia_de_cursos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um Aluno. 
 * Mapeada para a tabela 'aluno' no banco de dados.
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@JsonIgnoreProperties("turmas")

public class Aluno {

    /** Chave Primária, gerada automaticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome completo do Aluno. Não pode ser vazio. */
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    /** * Cadastro de Pessoa Física (CPF).
     * Requisito: O CPF deve ser único no sistema.
     * Usamos @Column(unique=true) para garantir a restrição no DB.
     * Usamos @Size para o tamanho (ex: 11 dígitos, ignorando formatação).
     */
    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres (incluindo formatação).")
    @Column(unique = true) 
    private String cpf;

    /** E-mail do Aluno. Não pode ser vazio e deve ter formato válido. */
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    private String email;

    // Obs: Esta classe futuramente terá o relacionamento N:M com 'Turma'.
}