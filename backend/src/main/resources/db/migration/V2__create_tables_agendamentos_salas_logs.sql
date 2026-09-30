-- V2__create_tables_agendamentos_salas_logs.sql

ALTER TABLE usuarios
    ADD COLUMN perfil_usuario VARCHAR(50) NOT NULL;

CREATE TABLE salas
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    disponivel  BOOLEAN NOT NULL,
    capacidade  INT NOT NULL,
    localizacao VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE agendamentos
(
    id                    BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id            BIGINT NOT NULL,
    sala_id               BIGINT NOT NULL,
    data_agendamento      DATETIME NOT NULL,
    hora_inicio           TIME NOT NULL,
    hora_fim              TIME NOT NULL,
    qntd_pessoas          INT NOT NULL,
    titulo                VARCHAR(255) NOT NULL,
    usuario_id_solicitante BIGINT NULL,
    status                VARCHAR(50) NOT NULL,
    ultima_atualizacao    DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_agendamento_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuarios (id),

    CONSTRAINT fk_agendamento_sala
        FOREIGN KEY (sala_id)
            REFERENCES salas (id),

    CONSTRAINT fk_agendamento_usuario_solicitante
        FOREIGN KEY (usuario_id_solicitante)
            REFERENCES usuarios (id)
);

CREATE TABLE logs
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    data_agendamento DATETIME NOT NULL,
    sala_agendamento BIGINT NOT NULL,
    hora_inicio_agendamento TIME NOT NULL,
    hora_fim_agendamento TIME NOT NULL,

    CONSTRAINT fk_log_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuarios(id),

    CONSTRAINT fk_log_sala
        FOREIGN KEY (sala_agendamento)
            REFERENCES salas(id)
);