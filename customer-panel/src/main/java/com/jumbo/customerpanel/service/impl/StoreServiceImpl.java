package com.jumbo.customerpanel.service.impl;

import com.jumbo.customerpanel.dto.StoreOutDto;
import com.jumbo.customerpanel.model.PageData;
import com.jumbo.customerpanel.service.StoreService;
import com.jumbo.map.entity.Store;
import com.jumbo.map.service.StoreManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreManagementService storeManagementService;

    @Transactional(readOnly = true)
    public PageData<StoreOutDto> search(String rsqlSearchParam, Point point, Pageable pageable) {
        return PageData.convert(storeManagementService.loadAllNearBy(rsqlSearchParam, point, pageable),
                (Store s) -> StoreOutDto.construct().map(s));
    }
}
