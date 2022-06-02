package com.jumbo.map.service.impl;

import com.jumbo.map.entity.Store;
import com.jumbo.map.model.Stores.StoreModel;
import com.jumbo.map.repository.StoreRepository;
import com.jumbo.map.service.StoreManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreManagementServiceImpl implements StoreManagementService {

    private final StoreRepository storeRepo;


    @Transactional
    public void batchPersist(List<StoreModel> stores) {
        storeRepo.saveAll(stores.stream()
                .map(Store::of)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public List<Store> loadAllNearBy(Point point, Pageable pageable) {
        return storeRepo.findByLocationNear(point, pageable);
    }
}
