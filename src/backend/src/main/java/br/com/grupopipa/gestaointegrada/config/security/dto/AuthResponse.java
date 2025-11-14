package br.com.grupopipa.gestaointegrada.config.security.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.grupopipa.gestaointegrada.cadastro.perfil.entity.PerfilModuloEntity;
import br.com.grupopipa.gestaointegrada.config.security.UserAuthenticated;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String username;
    private String name;
    private String token;
    private Collection<PermissaoModuloDTO> authorities;

    public AuthResponse(String token, String username, String name, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.username = username;
        this.name = name;
        this.authorities = buildPermissoes(authorities);
    }

    private Collection<PermissaoModuloDTO> buildPermissoes(Collection<? extends GrantedAuthority> authorities) {
        Map<String, PermissaoModuloDTO> permissoesPorModulo = new HashMap<>();

        for (GrantedAuthority authority : authorities) {
            String authorityString = authority.getAuthority();
            int lastUnderscore = authorityString.lastIndexOf('_');
            if (lastUnderscore > 0) {
                String moduloChave = authorityString.substring(0, lastUnderscore);
                String permissao = authorityString.substring(lastUnderscore + 1);

                PermissaoModuloDTO dto = permissoesPorModulo.computeIfAbsent(moduloChave, k -> {
                    PerfilModuloEntity pme = ((UserAuthenticated.CustomAuthority) authority).getPerfilModuloEntity();
                    return new PermissaoModuloDTO(pme.getModulo());
                });
                dto.addPermissao(permissao);
            }
        }
        return permissoesPorModulo.values();
    }
}
