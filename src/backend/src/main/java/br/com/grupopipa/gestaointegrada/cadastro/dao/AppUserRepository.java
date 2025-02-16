package br.com.grupopipa.gestaointegrada.cadastro.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.grupopipa.gestaointegrada.cadastro.entity.AppUserEntity;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, UUID>, JpaSpecificationExecutor<AppUserEntity> {

    public Optional<AppUserEntity> findAppUserByUsername(String username);

}
