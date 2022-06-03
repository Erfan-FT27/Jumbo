package com.jumbo.customerpanel.service.impl;

import com.jumbo.customerpanel.dto.StoreOutDto;
import com.jumbo.customerpanel.service.StoreService;
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
public class StoreServiceImpl implements StoreService {

    private final StoreManagementService storeManagementService;

    @Transactional(readOnly = true)
    public List<StoreOutDto> findAllStoreNearBy(Point point, Pageable pageable) {
        return storeManagementService.loadAllNearBy(point, pageable)
                .stream()
                .map(s -> StoreOutDto.construct().map(s))
                .collect(Collectors.toList());
    }
}
