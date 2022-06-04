package com.jumbo.map.service.initializer;

import com.jumbo.map.event.MapInitializedEvent;
import com.jumbo.map.service.impl.StoreManagementServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * MapInitializer will executed when context fully initialized by receiving ContextRefreshedEvent
 * it will call StoreInfoReader to load stores info, and call StoreManagementService for persist data
 * then it produces MapInitializedEvent to call MongoPreferenceInitializer
 */
@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class MapInitializer {

    private final StoreInfoReader storeInfoReader;
    private final StoreManagementServiceImpl storeManagementService;

    @EventListener(ContextRefreshedEvent.class)
    public void init(ContextRefreshedEvent event) {
        log.info("Calling storeInfoReader to load stores info");
        storeManagementService.batchPersist(storeInfoReader.read().getStores());
        log.info("Successfully persist stores info into DB");
        event.getApplicationContext().publishEvent(new MapInitializedEvent());
    }
}
