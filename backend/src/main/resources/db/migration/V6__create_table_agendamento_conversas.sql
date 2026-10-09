CREATE TABLE agendamento_conversas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    usuario_id BIGINT NOT NULL,

    status VARCHAR(50) NOT NULL,

    data_agendamento DATE,
    hora_inicio TIME,
    hora_fim TIME,
    qntd_pessoas INT,

    sala_id BIGINT NULL,

    titulo VARCHAR(255),

    CONSTRAINT fk_agendamento_conversas_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id),

    CONSTRAINT fk_agendamento_conversas_sala
        FOREIGN KEY (sala_id)
        REFERENCES salas(id)
);
