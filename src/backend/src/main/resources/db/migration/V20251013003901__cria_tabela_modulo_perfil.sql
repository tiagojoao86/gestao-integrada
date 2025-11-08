-- Tabela para armazenar os módulos do sistema (cadastrados manualmente)
CREATE TABLE modulo (
    id UUID NOT NULL,
    chave VARCHAR(50) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    grupo VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT pk_modulo PRIMARY KEY (id),
    CONSTRAINT uk_modulo_chave UNIQUE (chave)
);

COMMENT ON TABLE modulo IS 'Módulos do sistema para controle de acesso.';
COMMENT ON COLUMN modulo.chave IS 'Chave única textual para identificar o módulo no código.';
COMMENT ON COLUMN modulo.nome IS 'Nome amigável do módulo para exibição em tela.';
COMMENT ON COLUMN modulo.created_at IS 'Data de criação do registro (audit)';
COMMENT ON COLUMN modulo.updated_at IS 'Data da última modificação do registro (audit)';
COMMENT ON COLUMN modulo.created_by IS 'Usuário que criou o registro (audit)';
COMMENT ON COLUMN modulo.updated_by IS 'Usuário que atualizou o registro (audit)';

-- Tabela para os perfis de acesso
CREATE TABLE perfil (
    id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    nome VARCHAR(100) NOT NULL,
    CONSTRAINT pk_perfil PRIMARY KEY (id),
    CONSTRAINT uk_perfil_nome UNIQUE (nome)
);

COMMENT ON TABLE perfil IS 'Perfis de acesso que agrupam permissões.';

-- Tabela de associação entre Usuário e Perfil (N-para-N)
CREATE TABLE usuario_perfil (
    id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    usuario_id UUID NOT NULL,
    perfil_id UUID NOT NULL,
    CONSTRAINT pk_usuario_perfil PRIMARY KEY (id),
    CONSTRAINT fk_usuario_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_usuario_perfil_perfil FOREIGN KEY (perfil_id) REFERENCES perfil (id),
    CONSTRAINT uk_usuario_perfil UNIQUE (usuario_id, perfil_id)
);

COMMENT ON TABLE usuario_perfil IS 'Associa usuários aos seus perfis de acesso.';

-- Tabela de associação entre Perfil e Módulo, com as permissões (N-para-N)
CREATE TABLE perfil_modulo (
    id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    perfil_id UUID NOT NULL,
    modulo_id UUID NOT NULL,
    pode_listar BOOLEAN NOT NULL DEFAULT FALSE,
    pode_visualizar BOOLEAN NOT NULL DEFAULT FALSE,
    pode_editar BOOLEAN NOT NULL DEFAULT FALSE,
    pode_deletar BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_perfil_modulo PRIMARY KEY (id),
    CONSTRAINT fk_perfil_modulo_perfil FOREIGN KEY (perfil_id) REFERENCES perfil (id),
    CONSTRAINT fk_perfil_modulo_modulo FOREIGN KEY (modulo_id) REFERENCES modulo (id),
    CONSTRAINT uk_perfil_modulo UNIQUE (perfil_id, modulo_id)
);

COMMENT ON TABLE perfil_modulo IS 'Define as permissões de um perfil para um determinado módulo.';

INSERT INTO modulo (id, chave, nome, grupo)
VALUES (
        gen_random_uuid (),
        'USUARIO',
        'Cadastro de Usuários',
        'CADASTROS'
    ),
    (
        gen_random_uuid (),
        'PERFIL',
        'Perfis de Acesso',
        'CADASTROS'
    );