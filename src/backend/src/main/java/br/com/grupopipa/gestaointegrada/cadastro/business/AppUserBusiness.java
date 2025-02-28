package br.com.grupopipa.gestaointegrada.cadastro.business;

import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.grupopipa.gestaointegrada.cadastro.dao.AppUserRepository;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.AppUserDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.AppUserGridDTO;
import br.com.grupopipa.gestaointegrada.cadastro.entity.AppUserEntity;
import br.com.grupopipa.gestaointegrada.core.business.impl.CrudBusinessImpl;

@Service
public class AppUserBusiness
        extends CrudBusinessImpl<AppUserDTO, AppUserGridDTO, AppUserEntity, AppUserRepository> {

    private PasswordEncoder passwordEncoder;

    public AppUserBusiness(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AppUserDTO findAppUserDtoByUsername(String username) {
        AppUserEntity entity = this.repository.findAppUserByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot found user with username %s", username)));

        return buildDTOFromEntity(entity);
    }

    @Override
    protected AppUserEntity mergeEntityAndDTO(AppUserEntity entity, AppUserDTO dto) {
        if (Objects.isNull(entity)) {
            entity = new AppUserEntity();
        }

        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setPassword(generatePassword(dto.getPassword(), entity.getPassword()));
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
    protected AppUserDTO buildDTOFromEntity(AppUserEntity entity) {
        return AppUserDTO
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    @Override
    protected AppUserGridDTO buildGridDTOFromEntity(AppUserEntity entity) {
        return AppUserGridDTO
                .builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    protected List<String> getPropertiesToFilter() {
        return List.of("username", "name", "createdAt");
    }

    @Override
    protected Class<AppUserEntity> getEntityClass() {
        return AppUserEntity.class;
    }
}
