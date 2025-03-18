package br.com.grupopipa.gestaointegrada.core.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.grupopipa.gestaointegrada.cadastro.dao.Specifications;
import br.com.grupopipa.gestaointegrada.core.business.CrudBusiness;
import br.com.grupopipa.gestaointegrada.core.dto.DTO;
import br.com.grupopipa.gestaointegrada.core.dto.FilterDTO;
import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;
import br.com.grupopipa.gestaointegrada.core.entity.BaseEntity;
import br.com.grupopipa.gestaointegrada.core.enums.FilterLogicOperator;

@Service
public abstract class CrudBusinessImpl<D extends DTO, G extends GridDTO, T extends BaseEntity, R extends JpaRepository<T, UUID>>
        implements CrudBusiness<D, G> {

    @Autowired
    protected R repository;

    @Autowired
    private Specifications<T> specifications;

    public D save(D dto) {
        if (Objects.nonNull(dto.getId())) {
            T entity = this.findEntityById(dto.getId());

            if (Objects.nonNull(entity)) {
                return this.buildDTOFromEntity(repository.save(this.mergeEntityAndDTO(entity, dto)));
            }
        }
        return this.buildDTOFromEntity(repository.save(this.mergeEntityAndDTO(null, dto)));
    }

    public UUID delete(UUID id) {
        repository.deleteById(id);

        return id;
    }

    @SuppressWarnings("unchecked")
    public Page<G> list(FilterDTO filter, Pageable pageable) {
        Specification<T> specification = this.buildSpecification(filter);

        if (this.repository instanceof JpaSpecificationExecutor) {
            Page<T> page = ((JpaSpecificationExecutor<T>) this.repository).findAll(specification, pageable);
            return new PageImpl<>(
                    page.getContent().stream().map(this::buildGridDTOFromEntity).toList(),
                    page.getPageable(),
                    page.getTotalElements());
        }

        return null;

    }

    public D findById(UUID id) {
        Optional<T> optional = repository.findById(id);

        if (optional.isPresent()) {
            return buildDTOFromEntity(optional.get());
        }

        return null;
    }

    public T findEntityById(UUID id) {
        Optional<T> optional = repository.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    protected Specification<T> buildSpecification(FilterDTO filter) {
        if (!ObjectUtils.isEmpty(filter)) {
            if (FilterLogicOperator.AND.equals(filter.getFilterLogicOperator())) {
                return Specification.allOf(listSpecifications(filter));
            }

            return Specification.anyOf(listSpecifications(filter));
        }

        return null;
    }

    private List<Specification<T>> listSpecifications(FilterDTO filter) {
        List<Specification<T>> list = new ArrayList<>();
        for (String property : getPropertiesToFilter()) {
            Specification<T> newSpecification = specifications.withItem(filter.getItemByPropertyName(property),
                    getEntityClass());

            if (Objects.nonNull(newSpecification)) {
                list.add(newSpecification);
            }
        }

        return list;
    }

    protected abstract T mergeEntityAndDTO(T entity, D dto);

    protected abstract D buildDTOFromEntity(T entity);

    protected abstract G buildGridDTOFromEntity(T entity);

    protected abstract List<String> getPropertiesToFilter();

    protected abstract Class<T> getEntityClass();

}
