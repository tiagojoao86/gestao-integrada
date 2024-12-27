-- CREATE USER pipa WITH PASSWORD 'pipa123';

-- grant all privileges on schema pipa to pipa;

CREATE TABLE usuario (
    id uuid,
    nome VARCHAR,
    login VARCHAR,
    senha VARCHAR,
    criadoEm timestamp(6) NULL,
    criadoPor varchar(255) NULL,
    atualizadoEm timestamp(6) NULL,
    atualizadoPor varchar(255) NULL
);