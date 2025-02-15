package br.com.grupopipa.gestaointegrada.cadastro.business;

import java.util.List;

import org.springframework.stereotype.Service;
import br.com.grupopipa.gestaointegrada.cadastro.dao.UsuarioRepository;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.cadastro.entity.UsuarioEntity;
import br.com.grupopipa.gestaointegrada.core.business.impl.CrudBusinessImpl;

@Service
public class UsuarioBusiness
        extends CrudBusinessImpl<UsuarioDTO, UsuarioGridDTO, UsuarioEntity, UsuarioRepository> {

    @Override
    protected UsuarioEntity buildEntityFromDTO(UsuarioDTO dto) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(dto.getNome());
        entity.setLogin(dto.getLogin());
        entity.setId(dto.getId());

        return entity;
    }

    @Override
    protected UsuarioDTO buildDTOFromEntity(UsuarioEntity entity) {
        return UsuarioDTO
                .builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .login(entity.getLogin())
                .atualizadoEm(entity.getAtualizadoEm())
                .atualizadoPor(entity.getAtualizadoPor())
                .criadoEm(entity.getCriadoEm())
                .criadoPor(entity.getCriadoPor())
                .build();
    }

    @Override
    protected UsuarioGridDTO buildGridDTOFromEntity(UsuarioEntity entity) {
        return UsuarioGridDTO
                .builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .nome(entity.getNome())
                .criadoEm(entity.getCriadoEm())
                .build();
    }

    @Override
    protected List<String> getPropertiesToFilter() {
        return List.of("login", "nome", "criadoEm");
    }

    @Override
    protected Class<UsuarioEntity> getEntityClass() {
        return UsuarioEntity.class;
    }

    @Override
    protected UsuarioEntity mergeEntityAndDTO(UsuarioEntity entity, UsuarioDTO dto) {
        entity.setLogin(dto.getLogin());
        entity.setNome(dto.getNome());

        return entity;
    }

}
