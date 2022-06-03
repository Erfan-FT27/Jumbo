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

    @GetMapping("/search")
    @JsonView(Simple.class)
    public ActionResult<StoreOutDto> search(@Valid SearchModel model) {
        return ActionResult.<StoreOutDto>builder()
                .pageData(storeService.search(model.getPoint()
                        , model.createPage()))
                .success(true)
                .message("List of store successfully returned")
                .build();
    }

    @GetMapping("/detailed-info/search")
    @JsonView(Detailed.class)
    public CompletableFuture<ActionResult<StoreOutDto>> detailedSearch(@Valid SearchModel model) {
        return CompletableFuture.supplyAsync(() -> ActionResult.<StoreOutDto>builder()
                .pageData(storeService.search(model.getPoint()
                        , model.createPage()))
                .success(true)
                .message("Detailed list store successfully returned")
                .build(), detailedSearchServiceTaskExecutor);
    }
}
