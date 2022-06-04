package com.jumbo.customerpanel.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.jumbo.customerpanel.dto.StoreOutDto;
import com.jumbo.customerpanel.model.ActionResult;
import com.jumbo.customerpanel.model.SearchModel;
import com.jumbo.customerpanel.model.View.Detailed;
import com.jumbo.customerpanel.model.View.Simple;
import com.jumbo.customerpanel.service.impl.StoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

import static com.jumbo.customerpanel.config.DetailedSearchServiceConfig.Detailed_SEARCH_SERVICE_EXECUTOR;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreServiceImpl storeService;

    @Qualifier(Detailed_SEARCH_SERVICE_EXECUTOR)
    private final TaskExecutor detailedSearchServiceTaskExecutor;

    /**
     * Provide search mechanism for customers, that accepting SearchModel
     * This API capable of receiving RSQL format request alongside location query
     * and perform the nearest stores by combining them, also this API return data
     * in more simple format to reduce network cost and increase performance
     *
     * @param model
     * @return
     */
    @GetMapping("/search")
    @JsonView(Simple.class)
    public ActionResult<StoreOutDto> search(@Valid SearchModel model) {
        return ActionResult.<StoreOutDto>builder()
                .pageData(storeService.search(model.getSearch()
                        , model.getPoint()
                        , model.createPage()))
                .success(true)
                .message("List of store successfully returned")
                .build();
    }

    /**
     * This API has functionality exact as search API but it returned more detailed result
     * and it is protected by rate of request, this API simulates a expensive service
     *
     * @param model
     * @return
     */
    @GetMapping("/detailed-info/search")
    @JsonView(Detailed.class)
    public CompletableFuture<ActionResult<StoreOutDto>> detailedSearch(@Valid SearchModel model) {
        return CompletableFuture.supplyAsync(() -> ActionResult.<StoreOutDto>builder()
                .pageData(storeService.search(model.getSearch()
                        , model.getPoint()
                        , model.createPage()))
                .success(true)
                .message("Detailed list store successfully returned")
                .build(), detailedSearchServiceTaskExecutor);
    }
}
