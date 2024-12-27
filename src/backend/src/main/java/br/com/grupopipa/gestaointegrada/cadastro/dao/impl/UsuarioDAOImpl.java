package br.com.grupopipa.gestaointegrada.cadastro.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.grupopipa.gestaointegrada.cadastro.dao.UsuarioDAO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.Session;
import br.com.grupopipa.gestaointegrada.core.dao.FiltroUtil;
import br.com.grupopipa.gestaointegrada.core.dao.OrdenacaoUtil;
import br.com.grupopipa.gestaointegrada.core.dto.Filtro;
import br.com.grupopipa.gestaointegrada.core.dto.Ordem;
import br.com.grupopipa.gestaointegrada.flyway.db.postgresql.tables.Usuario;

@Component
public class UsuarioDAOImpl implements UsuarioDAO {

    @Autowired
    private DSLContext create;

    @Autowired
    private Session session;

    private final Usuario USUARIO = new Usuario();

    @Override
    public UsuarioDTO insert(UsuarioDTO dto) {
        LocalDateTime criadoEm = LocalDateTime.now();
        String criadoPor = session.getUser();
        dto.setId(UUID.randomUUID());

        return mapRowToDTO(create.insertInto(USUARIO)
                .set(USUARIO.ID, dto.getId())
                .set(USUARIO.NOME, dto.getNome())
                .set(USUARIO.LOGIN, dto.getLogin())
                .set(USUARIO.CRIADOEM,
                        criadoEm)
                .set(USUARIO.ATUALIZADOEM,
                        criadoEm)
                .set(USUARIO.CRIADOPOR,
                        criadoPor)
                .set(USUARIO.ATUALIZADOPOR,
                        criadoPor)
                .returning(USUARIO.asterisk())
                .fetchOne());
    }

    @Override
    public UsuarioDTO update(UsuarioDTO dto) {
        LocalDateTime atualizadoEm = LocalDateTime.now();
        String atualizadoPor = session.getUser();

        return mapRowToDTO(create
                .update(USUARIO)
                .set(USUARIO.NOME, dto.getNome())
                .set(USUARIO.LOGIN, dto.getLogin())
                .set(USUARIO.ATUALIZADOEM,
                        atualizadoEm)
                .set(USUARIO.ATUALIZADOPOR,
                        atualizadoPor)
                .where(USUARIO.ID.eq(dto.getId()))
                .returning(USUARIO.asterisk())
                .fetchOne());
    }

    @Override
    public UUID delete(UUID id) {
        create.delete(USUARIO).where(USUARIO.ID.eq(id)).execute();

        return id;
    }

    @Override
    public UsuarioGridDTO[] list(Filtro filtro, Integer paginaTamanho, Integer paginaNumero, List<Ordem> ordenacao) {
        SelectJoinStep<?> select = create
                .select(USUARIO.ID,
                        USUARIO.NOME,
                        USUARIO.LOGIN,
                        USUARIO.CRIADOEM)
                .from(USUARIO);

        if (Objects.nonNull(filtro)) {
            select.where(FiltroUtil.getConditionFromFiltro(filtro));
        }

        select.offset((paginaNumero - 1) * paginaTamanho)
                .limit(paginaTamanho);

        if (Objects.nonNull(ordenacao)) {
            for (Ordem ordem : ordenacao) {
                select.orderBy(OrdenacaoUtil.getOrdenacao(ordem.getPropriedade(), ordem.getDirecao()));
            }
        }

        return select
                .fetch()
                .stream()
                .map(this::mapRowToGridDTO)
                .toArray(UsuarioGridDTO[]::new);
    }

    @Override
    public Integer count(Filtro filtro) {
        SelectJoinStep<?> select = create.selectCount().from(USUARIO);

        if (Objects.nonNull(filtro)) {
            select.where(FiltroUtil.getConditionFromFiltro(filtro));
        }

        return select.fetchOne(0, Integer.class);
    }

    @Override
    public UsuarioDTO findById(UUID id) {
        UsuarioDTO dto = mapRowToDTO(create
                .select(USUARIO.asterisk())
                .from(USUARIO)
                .where(USUARIO.ID.eq(id))
                .fetchOne());

        return dto;
    }

    @Override
    public UsuarioDTO findByNome(String nome) {
        UsuarioDTO dto = mapRowToDTO(create
                .select(USUARIO.asterisk())
                .from(USUARIO)
                .where(USUARIO.NOME.eq(nome))
                .fetchOne());

        return dto;
    }

    private UsuarioDTO mapRowToDTO(Record record) {
        UsuarioDTO dto = UsuarioDTO.builder()
                .id(record.get(USUARIO.ID))
                .nome(record.get(USUARIO.NOME))
                .login(record.get(USUARIO.LOGIN))
                .criadoEm(record.get(USUARIO.CRIADOEM))
                .atualizadoEm(record.get(USUARIO.ATUALIZADOEM))
                .criadoPor(record.get(USUARIO.CRIADOPOR))
                .atualizadoPor(record.get(USUARIO.ATUALIZADOPOR))
                .build();

        return dto;
    }

    private UsuarioGridDTO mapRowToGridDTO(Record record) {
        UsuarioGridDTO dto = UsuarioGridDTO.builder()
                .id(record.get(USUARIO.ID))
                .nome(record.get(USUARIO.NOME))
                .login(record.get(USUARIO.LOGIN))
                .criadoEm(record.get(USUARIO.CRIADOEM))
                .build();

        return dto;
    }

}