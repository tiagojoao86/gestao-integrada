package br.com.grupopipa.gestaointegrada.cadastro.usuario.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.grupopipa.gestaointegrada.cadastro.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.core.entity.BaseEntity;
import br.com.grupopipa.gestaointegrada.core.exception.beanvalidation.BeanValidationException;
import br.com.grupopipa.gestaointegrada.core.exception.beanvalidation.BeanValidationMessage;
import br.com.grupopipa.gestaointegrada.core.validation.ValidationUtils;
import br.com.grupopipa.gestaointegrada.core.valueobject.Login;
import br.com.grupopipa.gestaointegrada.core.valueobject.Nome;
import br.com.grupopipa.gestaointegrada.core.valueobject.Senha;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity(name = "usuario")
public class UsuarioEntity extends BaseEntity {
    
    @Embedded
    private Nome nome;
    
    @Embedded
    private Login login;
    
    @Embedded
    private Senha senha;

    private UsuarioEntity(Nome nome, Login login, Senha senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    private UsuarioEntity() {
    }

    public String getNome() {
        return this.nome != null ? this.nome.getValue() : null;
    }

    public String getLogin() {
        return this.login != null ? this.login.getValue() : null;
    }

    public String getSenha() {
        return this.senha != null ? this.senha.getValue() : null;
    }

    private static class ValidatedData {
        final Nome nome;
        final Login login;
        final Senha senha;

        ValidatedData(Nome nome, Login login, Senha senha) {
            this.nome = nome;
            this.login = login;
            this.senha = senha;
        }
    }

    private static ValidatedData validate(String nomeStr, String loginStr, String senhaStr, PasswordEncoder passwordEncoder, boolean isCreation) {
        Set<BeanValidationMessage> violations = new HashSet<>();

        Nome nome = ValidationUtils.validateAndGet(() -> Nome.of(nomeStr), violations);
        Login login = ValidationUtils.validateAndGet(() -> Login.of(loginStr), violations);
        
        Senha senha = null;
        if (isCreation) {            
            senha = ValidationUtils.validateAndGet(() -> Senha.of(senhaStr, passwordEncoder), violations);
        } else if (senhaStr != null && !senhaStr.trim().isEmpty()) {            
            senha = ValidationUtils.validateAndGet(() -> Senha.of(senhaStr, passwordEncoder), violations);
        }

        if (!violations.isEmpty()) {
            throw new BeanValidationException("usuario", violations);
        }
        return new ValidatedData(nome, login, senha);
    }

    public void updateUsuarioFromDTO(UsuarioDTO dto, PasswordEncoder passwordEncoder) {
        ValidatedData data = validate(dto.getNome(), dto.getLogin(), dto.getSenha(), passwordEncoder, false);
        this.nome = data.nome;
        this.login = data.login;
        if (Objects.nonNull(data.senha)) {
            this.senha = data.senha;
        }
    }

    public static class Builder {

        private String login;
        private String nome;
        private String senha;        

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder senha(String senha) {
            this.senha = senha;
            return this;
        }

        public UsuarioEntity build(PasswordEncoder passwordEncoder) {
            ValidatedData data = validate(this.nome, this.login, this.senha, passwordEncoder, true);
            return new UsuarioEntity(data.nome, data.login, data.senha);
        }
    }
}
