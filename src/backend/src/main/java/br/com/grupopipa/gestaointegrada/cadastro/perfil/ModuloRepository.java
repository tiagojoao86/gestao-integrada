package br.com.grupopipa.gestaointegrada.cadastro.perfil;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grupopipa.gestaointegrada.cadastro.perfil.entity.ModuloEntity;

public interface ModuloRepository extends JpaRepository<ModuloEntity, UUID> {
}