ALTER TABLE usuarios
    CHANGE COLUMN username email VARCHAR(255) NOT NULL;

ALTER TABLE usuarios
    ADD COLUMN nome VARCHAR(255) NOT NULL;

ALTER TABLE usuarios
DROP INDEX username,
    ADD CONSTRAINT uk_usuarios_email UNIQUE (email);