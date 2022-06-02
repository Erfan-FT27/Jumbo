package com.jumbo.customerpanel.service;

import com.jumbo.map.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

import java.util.List;

public interface StoreService {

    List<Store> findAllStoreNearBy(Point point, Pageable pageable);
}
