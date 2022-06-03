package com.jumbo.customerpanel.service;

import com.jumbo.customerpanel.config.PageData;
import com.jumbo.customerpanel.dto.StoreOutDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

public interface StoreService {

    PageData<StoreOutDto> search(Point point, Pageable pageable);
}
