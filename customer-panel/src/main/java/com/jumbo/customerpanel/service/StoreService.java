package com.jumbo.customerpanel.service;

import com.jumbo.customerpanel.dto.StoreOutDto;
import com.jumbo.customerpanel.model.PageData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

public interface StoreService {

    PageData<StoreOutDto> search(String rsqlSearchParam, Point point, Pageable pageable);
}
