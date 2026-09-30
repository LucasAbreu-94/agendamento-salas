package br.com.foursys.agendamento_salas.domain;

import br.com.foursys.agendamento_salas.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfilUsuario;
}