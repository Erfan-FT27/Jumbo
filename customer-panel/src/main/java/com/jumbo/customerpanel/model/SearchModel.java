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
import org.springframework.data.geo.Point;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.jumbo.customerpanel.Constant.*;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchModel implements Serializable {

    private static final long serialVersionUID = 256095691125585935L;

    @NotNull(message = "in.search.model.longitude.could.not.empty")
    @Min(value = LONGITUDE_MIN_SIZE, message = "in.search.model.longitude.min.size")
    @Max(value = LONGITUDE_MAX_SIZE, message = "in.search.model.longitude.max.size")
    private Double longitude;

    @NotNull(message = "in.search.model.latitude.could.not.empty")
    @Min(value = LATITUDE_MIN_SIZE, message = "in.search.model.latitude.min.size")
    @Max(value = LATITUDE_MAX_SIZE, message = "in.search.model.latitude.max.size")
    private Double latitude;

    @Min(value = 0, message = "in.search.model.pageSize.min.size")
    @Max(value = MAX_PAGE_SIZE, message = "in.search.model.pageSize.max.size")
    private Integer pageSize;

    @Min(value = 0, message = "in.search.model.pageNumber.min.size")
    @Max(value = MAX_PAGE_NUMBER, message = "in.search.model.pageNumber.max.size")
    private Integer pageNumber;

    private String search;

    private Direction direction;

    @Size(max = MAX_ORDER_PROPERTIES_SIZE, message = "in.search.model.orderByProperties.size.limit")
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

    @JsonIgnore
    public Point getPoint(){
        return new Point(getLongitude(), getLatitude());
    }
}
