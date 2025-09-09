-- CREATE USER pipa WITH PASSWORD 'pipa123';

-- grant all privileges on schema pipa to pipa;

-- admin password: @RLthotr$&u=Huge1e-r

-- Script para criar a tabela 'usuario' no PostgreSQL

-- Remove a tabela se ela já existir, para facilitar a recriação durante o desenvolvimento.
-- Em produção, esta linha deve ser usada com cautela.
DROP TABLE IF EXISTS usuario;

-- Cria a tabela 'usuario'
-- As colunas id, created_at, etc., são uma suposição baseada na existência
-- de uma 'BaseEntity' no seu projeto Java.

CREATE TABLE usuario (
    id          UUID            NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),

    nome        VARCHAR(255)    NOT NULL,
    login       VARCHAR(100)    NOT NULL,
    senha       VARCHAR(255)    NOT NULL,

-- Constraint para a chave primária com nome definido
CONSTRAINT pk_usuario PRIMARY KEY (id),

-- Constraint de unicidade para a coluna 'login' com nome definido
CONSTRAINT uk_usuario_login UNIQUE (login) );

-- Adiciona constraints de verificação (CHECK) para garantir que os campos não sejam brancos/vazios.
-- Isto é mais robusto que apenas NOT NULL, pois também impede strings que contenham apenas espaços.
-- O tratamento de erro no backend pode capturar o nome da constraint para dar uma mensagem específica.

-- Constraint para o campo 'nome' não ser branco/vazio
ALTER TABLE usuario
ADD CONSTRAINT ck_usuario_nome_not_blank CHECK (
    trim(
        both
        from nome
    ) <> ''
);

-- Constraint para o campo 'login' não ser branco/vazio
ALTER TABLE usuario
ADD CONSTRAINT ck_usuario_login_not_blank CHECK (
    trim(
        both
        from login
    ) <> ''
);

-- Adiciona comentários na tabela e colunas para documentação do banco de dados.
COMMENT ON
TABLE usuario IS 'Tabela para armazenar os usuários do sistema.';
COMMENT ON COLUMN usuario.id IS 'Identificador único do usuário (Chave primária).';
COMMENT ON COLUMN usuario.created_at IS 'Data e hora de criação do registro.';
COMMENT ON COLUMN usuario.updated_at IS 'Data e hora da última atualização do registro.';
COMMENT ON COLUMN usuario.created_by IS 'Usuário que criou o registro.';
COMMENT ON COLUMN usuario.updated_by IS 'Usuário que realizou a última atualização.';
COMMENT ON COLUMN usuario.nome IS 'Nome completo do usuário.';
COMMENT ON COLUMN usuario.login IS 'Login único utilizado pelo usuário para acessar o sistema.';
COMMENT ON COLUMN usuario.senha IS 'Senha criptografada do usuário.';

INSERT INTO usuario (id, nome, login, senha, created_at, created_by, updated_at, updated_by)
VALUES ('018f4a9b-704a-7b04-8e35-05a135f0a29c'::uuid, 'administrador', 'admin', '$2a$12$SrJbdt97EgaaXAfyuzBue.27hOy13nbBbMAj2d0QVsCjAQcLHD/XK', now(), 'admin', null, null);