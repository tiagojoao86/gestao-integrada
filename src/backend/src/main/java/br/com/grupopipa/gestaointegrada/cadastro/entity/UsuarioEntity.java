package br.com.grupopipa.gestaointegrada.cadastro.entity;

import br.com.grupopipa.gestaointegrada.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "usuario")
public class UsuarioEntity extends BaseEntity {

    private String nome;
    private String login;
    private String senha;

    public UsuarioEntity() {
    }
}
