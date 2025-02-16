package br.com.grupopipa.gestaointegrada.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.grupopipa.gestaointegrada.cadastro.dao.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AppUserRepository repository;

    public UserDetailsServiceImpl(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findAppUserByUsername(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

}