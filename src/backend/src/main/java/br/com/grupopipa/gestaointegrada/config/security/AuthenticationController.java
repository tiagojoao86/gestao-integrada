package br.com.grupopipa.gestaointegrada.config.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.service.UsuarioEntityService;
import br.com.grupopipa.gestaointegrada.core.controller.Response;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import static br.com.grupopipa.gestaointegrada.cadastro.constants.Constants.R_AUTHENTICATE;
import static br.com.grupopipa.gestaointegrada.core.controller.Response.forbidden;
import static br.com.grupopipa.gestaointegrada.core.controller.Response.ok;

@RestController
@RequestMapping(R_AUTHENTICATE)
public class AuthenticationController {

    private AuthenticationService authenticationService;
    private UsuarioEntityService usuarioBusiness;
    private AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationService authenticationService, 
            UsuarioEntityService usuarioEntityBusiness,
            AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.usuarioBusiness = usuarioEntityBusiness;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public Response authenticate(
            @RequestBody AuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String accessToken = authenticationService.authenticate(authentication.getName(),
                authentication.getAuthorities());

        String refreshToken = authenticationService.generateRefreshToken(authentication.getName(),
                authentication.getAuthorities());

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/authenticate/refresh");
        response.addCookie(refreshCookie);

        UsuarioDTO userDTO = this.usuarioBusiness.findUsuarioDTOByLogin(authentication.getName());

        AuthResponse authResponse = new AuthResponse(accessToken, userDTO.getLogin(), userDTO.getNome());

        return ok(authResponse);

    }

    @PostMapping("refresh")
    public Response refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !authenticationService.validateToken(refreshToken)) {
            return forbidden("Refresh token invalid or expired");
        }

        String username = authenticationService.getUsernameFromToken(refreshToken);
        String newAccessToken = authenticationService.authenticate(username, List.of(() -> "read"));

        UsuarioDTO userDTO = this.usuarioBusiness.findUsuarioDTOByLogin(username);

        AuthResponse authResponse = new AuthResponse(newAccessToken, userDTO.getLogin(), userDTO.getNome());

        return ok(authResponse);
    }
}

@Getter
class AuthResponse {
    private String username;
    private String name;
    private String token;

    public AuthResponse(String token, String username, String name) {
        this.token = token;
        this.username = username;
        this.name = name;
    }
}

@Getter
class AuthRequest {
    private String username;
    private String password;
}