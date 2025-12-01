package com.cursos.gerencia_de_cursos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um Curso. 
 * Mapeada para a tabela 'curso' no banco de dados.
 */
@Entity
// Lombok para gerar getters, setters, toString, equals e hashCode (Slide 17)
@Data 
// Construtor sem argumentos (requisito JavaBean, Slide 11)
@NoArgsConstructor 
// Construtor com todos os argumentos
@AllArgsConstructor 
public class Curso {

    /** Chave Primária, gerada automaticamente pelo banco (Slide 15). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do Curso. Não pode ser vazio e tem um tamanho máximo razoável. */
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    /** Descrição detalhada do Curso. Não pode ser vazia. */
    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    /** Carga horária em horas. Deve ser maior ou igual a 1 hora. */
    @NotNull(message = "A carga horária é obrigatória.")
    @Min(value = 1, message = "A carga horária deve ser de no mínimo 1 hora.")
    private Integer cargaHoraria;

    // Obs: Esta classe futuramente terá o relacionamento 1:N com 'Turma'.
}