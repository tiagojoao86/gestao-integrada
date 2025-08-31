-- CREATE USER pipa WITH PASSWORD 'pipa123';

-- grant all privileges on schema pipa to pipa;

-- admin password: @RLthotr$&u=Huge1e-r

CREATE TABLE usuario (
    id uuid primary key,
    nome VARCHAR NOT NULL,
    login VARCHAR UNIQUE NOT NULL,
    senha VARCHAR NOT NULL,
    createdAt timestamp(6) NULL,
    createdBy varchar(255) NULL,
    updatedAt timestamp(6) NULL,
    updatedBy varchar(255) NULL
);

INSERT INTO usuario (id, nome, login, senha, createdAt, createdBy, updatedAt, updatedBy)
VALUES ('e5fc9426-5933-464f-a2be-27734a351678', 'administrador', 'admin', '$2a$12$SrJbdt97EgaaXAfyuzBue.27hOy13nbBbMAj2d0QVsCjAQcLHD/XK', now(), 'admin', null, null);