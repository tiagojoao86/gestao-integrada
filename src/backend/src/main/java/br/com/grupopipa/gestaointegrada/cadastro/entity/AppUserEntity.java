package br.com.grupopipa.gestaointegrada.cadastro.entity;

import br.com.grupopipa.gestaointegrada.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "appuser")
public class AppUserEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public AppUserEntity() {
    }
}
