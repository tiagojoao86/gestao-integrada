package br.com.grupopipa.gestaointegrada.cadastro.perfil.entity;

import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.valueobject.Nome;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "modulo_grupo")
public class ModuloGrupoEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private Nome nome;
    
    protected ModuloGrupoEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return this.nome != null ? this.nome.getValue() : null;
    }
}
