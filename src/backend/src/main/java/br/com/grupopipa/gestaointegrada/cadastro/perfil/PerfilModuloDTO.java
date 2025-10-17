package br.com.grupopipa.gestaointegrada.cadastro.perfil;

import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.valueobject.Nome;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilModuloDTO {

    private UUID moduloId;
    private String moduloNome;
    private boolean podeListar;
    private boolean podeVisualizar;
    private boolean podeEditar;
    private boolean podeDeletar;

}