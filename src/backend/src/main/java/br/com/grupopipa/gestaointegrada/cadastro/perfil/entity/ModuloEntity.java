package br.com.grupopipa.gestaointegrada.cadastro.perfil.entity;

import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.valueobject.Chave;
import br.com.grupopipa.gestaointegrada.core.valueobject.Nome;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "modulo")
public class ModuloEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private Chave chave;

    @Embedded
    private Nome nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private ModuloGrupoEntity grupo;

    protected ModuloEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getChave() {
        return this.chave != null ? this.chave.getValue() : null;
    }

    public String getNome() {
        return this.nome != null ? this.nome.getValue() : null;
    }

    public ModuloGrupoEntity getGrupo() {
        return grupo;
    }
}
