package br.com.grupopipa.gestaointegrada.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.grupopipa.gestaointegrada.cadastro.dao.UsuarioEntityRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsuarioEntityRepository repository;

    public UserDetailsServiceImpl(UsuarioEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findUsuarioByLogin(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

}