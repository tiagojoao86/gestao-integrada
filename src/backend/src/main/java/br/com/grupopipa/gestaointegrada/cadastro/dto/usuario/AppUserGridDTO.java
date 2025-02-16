package br.com.grupopipa.gestaointegrada.cadastro.dto.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AppUserGridDTO implements GridDTO {
    private UUID id;
    private String name;
    private String username;
    private LocalDateTime createdAt;
}