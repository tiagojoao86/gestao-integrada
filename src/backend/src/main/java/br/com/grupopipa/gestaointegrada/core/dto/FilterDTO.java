package br.com.grupopipa.gestaointegrada.core.dto;

import java.util.List;

import org.springframework.util.ObjectUtils;

import br.com.grupopipa.gestaointegrada.core.enums.FilterLogicOperator;

import lombok.Getter;

@Getter
public class FilterDTO {
    private FilterLogicOperator filterLogicOperator;
    private List<FilterItemDTO> items;

    public FilterItemDTO getItemByPropertyName(String property) {
        if (ObjectUtils.isEmpty(items)) {
            return null;
        }

        List<FilterItemDTO> filteredList = items.stream().filter(it -> it.getProperty().equals(property)).toList();

        if (!ObjectUtils.isEmpty(filteredList)) {
            return filteredList.get(0);
        }

        return null;
    }
}
