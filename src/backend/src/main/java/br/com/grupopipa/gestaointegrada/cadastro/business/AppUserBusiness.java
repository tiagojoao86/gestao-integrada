package br.com.grupopipa.gestaointegrada.cadastro.business;

import java.util.List;

import org.springframework.stereotype.Service;
import br.com.grupopipa.gestaointegrada.cadastro.dao.AppUserRepository;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.AppUserDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.AppUserGridDTO;
import br.com.grupopipa.gestaointegrada.cadastro.entity.AppUserEntity;
import br.com.grupopipa.gestaointegrada.core.business.impl.CrudBusinessImpl;

@Service
public class AppUserBusiness
        extends CrudBusinessImpl<AppUserDTO, AppUserGridDTO, AppUserEntity, AppUserRepository> {

    @Override
    protected AppUserEntity buildEntityFromDTO(AppUserDTO dto) {
        AppUserEntity entity = new AppUserEntity();
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setPassword(getSenha(dto.getPassword()));
        entity.setPassword(dto.getPassword());
        entity.setId(dto.getId());

        return entity;
    }

    private String getSenha(String plainPassword) {
        return "";
    }

    private boolean hasChangedSenha(String password) {
        return false;
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

    @Override
    protected AppUserEntity mergeEntityAndDTO(AppUserEntity entity, AppUserDTO dto) {
        entity.setUsername(dto.getUsername());
        entity.setName(dto.getName());

        return entity;
    }

}
