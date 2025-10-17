package br.com.grupopipa.gestaointegrada.cadastro.perfil;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.grupopipa.gestaointegrada.cadastro.perfil.entity.PerfilModuloEntity;

public interface PerfilModuloRepository extends JpaRepository<PerfilModuloEntity, UUID> {

    void deleteByPerfilId(UUID perfilId);

    List<PerfilModuloEntity> findByPerfilId(UUID perfilId);
}