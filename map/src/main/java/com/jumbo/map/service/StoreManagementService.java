package com.jumbo.map.service;

import com.jumbo.map.entity.Store;
import com.jumbo.map.model.Stores;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

import java.util.List;

public interface StoreManagementService {

    void batchPersist(List<Stores.StoreModel> stores);

    List<Store> loadAllNearBy(Point point, Pageable pageable);
}
