package com.cursos.gerencia_de_cursos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um Professor (Instrutor). 
 * Mapeada para a tabela 'professor' no banco de dados.
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@JsonIgnoreProperties("turmas")

public class Professor {

    /** Chave Primária, gerada automaticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome completo do Professor. Não pode ser vazio. */
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    /** Área de Especialização do Professor (e.g., 'Desenvolvimento Web', 'UX/UI'). */
    @NotBlank(message = "A área de especialização é obrigatória.")
    private String areaEspecializacao;

    /** * Currículo resumido do Professor.
     * Pode ter um limite de tamanho maior.
     */
    @NotBlank(message = "O currículo é obrigatório.")
    @Size(max = 500, message = "O currículo não deve exceder 500 caracteres.")
    private String curriculo;

    // Obs: Esta classe futuramente terá o relacionamento 1:N com 'Turma'.
}