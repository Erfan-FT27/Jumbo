package com.jumbo.customerpanel.controller;

import com.jumbo.customerpanel.model.SearchModel;
import com.jumbo.customerpanel.service.impl.StoreServiceImpl;
import com.jumbo.map.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreServiceImpl storeService;

    @GetMapping("/search")
    public List<Store> findAllStoreNearBy(@Valid SearchModel model) {
        return storeService
                .findAllStoreNearBy(
                        new Point(model.getLongitude(), model.getLatitude())
                        , model.createPage());
    }
}
