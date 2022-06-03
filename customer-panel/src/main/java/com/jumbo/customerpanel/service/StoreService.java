package com.jumbo.customerpanel.service;

import com.jumbo.customerpanel.dto.StoreOutDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

import java.util.List;

public interface StoreService {

    List<StoreOutDto> findAllStoreNearBy(Point point, Pageable pageable);
}
