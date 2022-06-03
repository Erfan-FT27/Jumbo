package com.jumbo.customerpanel.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.jumbo.customerpanel.config.View.Detailed;
import com.jumbo.customerpanel.config.View.Simple;
import com.jumbo.customerpanel.dto.StoreOutDto;
import com.jumbo.customerpanel.model.SearchModel;
import com.jumbo.customerpanel.service.impl.StoreServiceImpl;
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
    @JsonView(Simple.class)
    public List<StoreOutDto> search(@Valid SearchModel model) {
        return storeService
                .findAllStoreNearBy(
                        new Point(model.getLongitude(), model.getLatitude())
                        , model.createPage());
    }

    @GetMapping("/detailed-info/search")
    @JsonView(Detailed.class)
    public List<StoreOutDto> findAllStoreNearBy(@Valid SearchModel model) {
        return storeService
                .findAllStoreNearBy(
                        new Point(model.getLongitude(), model.getLatitude())
                        , model.createPage());
    }
}
