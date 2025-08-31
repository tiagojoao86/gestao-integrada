package br.com.grupopipa.gestaointegrada.cadastro.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.grupopipa.gestaointegrada.cadastro.entity.UsuarioEntity;

@Repository
public interface UsuarioEntityRepository extends JpaRepository<UsuarioEntity, UUID>, JpaSpecificationExecutor<UsuarioEntity> {

    public Optional<UsuarioEntity> findUsuarioByLogin(String login);

}
