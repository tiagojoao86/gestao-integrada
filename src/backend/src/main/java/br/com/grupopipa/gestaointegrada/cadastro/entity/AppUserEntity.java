package br.com.grupopipa.gestaointegrada.cadastro.entity;

import br.com.grupopipa.gestaointegrada.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "appuser")
public class AppUserEntity extends BaseEntity {

    private String name;
    private String username;
    private String password;

    public AppUserEntity() {
    }
}
