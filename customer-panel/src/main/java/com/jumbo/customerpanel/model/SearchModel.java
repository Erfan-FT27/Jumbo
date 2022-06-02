package com.jumbo.customerpanel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.io.Serializable;

import static com.jumbo.customerpanel.Constant.DEFAULT_PAGE_NUMBER;
import static com.jumbo.customerpanel.Constant.DEFAULT_PAGE_SIZE;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchModel implements Serializable {

    private static final long serialVersionUID = 256095691125585935L;

    private Double longitude;
    private Double latitude;

    private Integer pageSize;

    private Integer pageNumber;

    private Direction direction;

    private String[] orderByProperties;


    public Pageable createPage() {
        if (getOrderByProperties() == null || getOrderByProperties().length == 0)
            return PageRequest.of(getPageNumberOrDefault(), getPageSizeOrDefault());
        return PageRequest.of(getPageNumberOrDefault(), getPageSizeOrDefault(),
                Sort.by(getDirectionOrDefault(), getOrderByProperties()));
    }


    @JsonIgnore
    private Integer getPageSizeOrDefault() {
        return pageSize != null && pageSize != 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    @JsonIgnore
    private Integer getPageNumberOrDefault() {
        return pageNumber != null && pageNumber != 0 ? pageNumber : DEFAULT_PAGE_NUMBER;
    }

    @JsonIgnore
    private Direction getDirectionOrDefault() {
        return direction != null ? direction : Direction.ASC;
    }
}
