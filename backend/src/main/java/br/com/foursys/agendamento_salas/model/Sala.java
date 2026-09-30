package br.com.foursys.agendamento_salas.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "salas")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean disponivel;
    private Integer capacidade;
    private String localizacao;
}
