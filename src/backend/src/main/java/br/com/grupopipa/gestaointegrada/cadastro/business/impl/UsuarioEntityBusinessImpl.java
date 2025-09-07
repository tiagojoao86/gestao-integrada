package br.com.grupopipa.gestaointegrada.cadastro.business.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.grupopipa.gestaointegrada.cadastro.business.UsuarioEntityBusiness;
import br.com.grupopipa.gestaointegrada.cadastro.dao.UsuarioEntityRepository;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.cadastro.entity.UsuarioEntity;
import br.com.grupopipa.gestaointegrada.core.business.impl.CrudBusinessImpl;

@Service
public class UsuarioEntityBusinessImpl
        extends CrudBusinessImpl<UsuarioDTO, UsuarioGridDTO, UsuarioEntity, UsuarioEntityRepository>
        implements UsuarioEntityBusiness {

    private PasswordEncoder passwordEncoder;

    public UsuarioEntityBusinessImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioDTO findUsuarioDtoByLogin(String login) {
        UsuarioEntity entity = this.repository.findUsuarioByLogin(login)
                .orElseThrow(() -> new RuntimeException(String.format("Não foi possível localizar o usuário com o login %s", login)));

        return buildDTOFromEntity(entity);
    }

    @Override
    protected UsuarioEntity mergeEntityAndDTO(UsuarioEntity entity, UsuarioDTO dto) {
        if (Objects.isNull(entity)) {
            entity = new UsuarioEntity();
        }

        entity.setNome(dto.getNome());
        entity.setLogin(dto.getLogin());
        entity.setSenha(generatePassword(dto.getSenha(), entity.getSenha()));
        entity.setId(dto.getId());

        return entity;
    }

    private String generatePassword(String dtoPassword, String entityPassword) {
        if (ObjectUtils.isEmpty(entityPassword) || !dtoPassword.equals(entityPassword)) {
            return passwordEncoder.encode(dtoPassword);
        }

        return entityPassword;
    }

    @Override
    protected UsuarioDTO buildDTOFromEntity(UsuarioEntity entity) {
        return UsuarioDTO
                .builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .login(entity.getLogin())
                .senha(entity.getSenha())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    @Override
    protected UsuarioGridDTO buildGridDTOFromEntity(UsuarioEntity entity) {
        return UsuarioGridDTO
                .builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .nome(entity.getNome())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    protected List<String> getPropertiesToFilter() {
        return List.of("login", "nome", "createdAt");
    }

    @Override
    protected Class<UsuarioEntity> getEntityClass() {
        return UsuarioEntity.class;
    }
}
