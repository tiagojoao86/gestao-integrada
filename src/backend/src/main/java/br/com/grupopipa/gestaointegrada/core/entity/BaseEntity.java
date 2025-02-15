package br.com.grupopipa.gestaointegrada.core.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.Session;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    private String criadoPor;
    private String atualizadoPor;

    public BaseEntity() {
    }

    @PrePersist
    public void create() {
        LocalDateTime now = LocalDateTime.now();
        this.criadoEm = now;
        this.atualizadoEm = now;
        this.criadoPor = Session.getUser();
    }

    @PreUpdate
    public void update() {
        LocalDateTime now = LocalDateTime.now();
        String user = Session.getUser();

        if (Objects.isNull(criadoEm)) {
            this.criadoEm = now;
        }

        if (Objects.isNull(this.criadoPor)) {
            this.criadoPor = user;
        }

        this.atualizadoEm = now;
        this.atualizadoPor = user;
    }

}