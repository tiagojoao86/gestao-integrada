package br.com.grupopipa.gestaointegrada.config.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.grupopipa.gestaointegrada.cadastro.usuario.entity.UsuarioEntity;

public class UserAuthenticated implements UserDetails {

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
    return List.of(() -> "read");
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
