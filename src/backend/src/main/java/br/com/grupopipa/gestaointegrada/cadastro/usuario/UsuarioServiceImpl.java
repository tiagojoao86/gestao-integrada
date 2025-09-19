package br.com.grupopipa.gestaointegrada.cadastro.usuario;

import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.grupopipa.gestaointegrada.cadastro.usuario.entity.UsuarioEntity;
import br.com.grupopipa.gestaointegrada.core.dao.Specifications;
import br.com.grupopipa.gestaointegrada.core.exception.EntityNotFoundException;
import br.com.grupopipa.gestaointegrada.core.service.impl.CrudServiceImpl;

@Service
public class UsuarioServiceImpl
        extends CrudServiceImpl<UsuarioDTO, UsuarioGridDTO, UsuarioEntity, UsuarioRepository>
        implements UsuarioService {

    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository repository,
            Specifications<UsuarioEntity> specifications) {
        super(repository, specifications);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioDTO findUsuarioDTOByLogin(String login) {
        UsuarioEntity entity = this.repository.findUsuarioByLoginValue(login)
                .orElseThrow(() -> new EntityNotFoundException(getEntityClass().getSimpleName(), "login", login));

        return buildDTOFromEntity(entity);
    }

    @Override
    protected UsuarioEntity mergeEntityAndDTO(UsuarioEntity entity, UsuarioDTO dto) {
        if (Objects.isNull(entity)) {
            return new UsuarioEntity.Builder()
                    .nome(dto.getNome())
                    .login(dto.getLogin())
                    .senha(dto.getSenha())
                    .build(this.passwordEncoder);
        }

        entity.updateUsuarioFromDTO(dto, passwordEncoder);
        return entity;
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
