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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    public BaseEntity() {
    }

    @PrePersist
    public void create() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = Session.getAppUserUsername();
    }

    @PreUpdate
    public void update() {
        LocalDateTime now = LocalDateTime.now();
        String user = Session.getAppUserUsername();

        if (Objects.isNull(createdAt)) {
            this.createdAt = now;
        }

        if (Objects.isNull(this.createdBy)) {
            this.createdBy = user;
        }

        this.updatedAt = now;
        this.updatedBy = user;
    }

}