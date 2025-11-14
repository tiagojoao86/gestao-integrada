package br.com.grupopipa.gestaointegrada.config.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.grupopipa.gestaointegrada.cadastro.perfil.entity.PerfilEntity;
import br.com.grupopipa.gestaointegrada.cadastro.perfil.entity.PerfilModuloEntity;
import br.com.grupopipa.gestaointegrada.cadastro.perfil.entity.UsuarioPerfilEntity;
import br.com.grupopipa.gestaointegrada.cadastro.usuario.entity.UsuarioEntity;
import lombok.Getter;

public class UserAuthenticated implements UserDetails {

  @Getter
  public static class CustomAuthority implements GrantedAuthority {
    private final PerfilModuloEntity perfilModuloEntity;
    private final String authority;

    public CustomAuthority(String role, PerfilModuloEntity perfilModuloEntity) {
      this.authority = role;
      this.perfilModuloEntity = perfilModuloEntity;
    }

    @Override
    public String getAuthority() {
      return this.authority;
    }
  }

  private final UsuarioEntity user;

  public UserAuthenticated(UsuarioEntity user) {
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getLogin();
  }

  @Override
  public String getPassword() {
    return user.getSenha();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<>();
    for (UsuarioPerfilEntity usuarioPerfil : user.getPerfis()) {
      PerfilEntity perfil = usuarioPerfil.getPerfil();
      if (perfil != null) {
        for (PerfilModuloEntity permissao : perfil.getPermissoes()) {
          String moduloChave = permissao.getModulo().getChave();
          if (permissao.isPodeListar()) {
            authorities.add(new CustomAuthority(moduloChave + "_LISTAR", permissao));
          }
          if (permissao.isPodeVisualizar()) {
            authorities.add(new CustomAuthority(moduloChave + "_VISUALIZAR", permissao));
          }
          if (permissao.isPodeEditar()) {
            authorities.add(new CustomAuthority(moduloChave + "_EDITAR", permissao));
          }
          if (permissao.isPodeDeletar()) {
            authorities.add(new CustomAuthority(moduloChave + "_DELETAR", permissao));
          }
        }
      }
    }
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
