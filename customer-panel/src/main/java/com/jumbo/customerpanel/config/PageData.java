package com.jumbo.customerpanel.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 822691125585935L;

    private List<T> items;
    private Integer totalPages;
    private Long totalElements;

    public static <T,U> PageData<U> convert(Page<T> page, Function<T, U> function) {
        PageData<U> pageData = new PageData<>();
        pageData.setItems(page.getContent().stream().map(function).collect(Collectors.toList()));
        pageData.setTotalElements(page.getTotalElements());
        pageData.setTotalPages(page.getTotalPages());
        return pageData;
    }
}