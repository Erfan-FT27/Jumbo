package com.jumbo.map.service.initializer;

import com.jumbo.map.entity.Store;
import com.jumbo.map.event.MapInitializedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE;

/**
 * MongoPreferenceInitializer will called when recieved MapInitializedEvent and has a duty
 * to add location index for store entity
 */
@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class MongoPreferenceInitializer {

    private final MongoTemplate mongoTemplate;

    @EventListener(MapInitializedEvent.class)
    public void init(MapInitializedEvent event) {
        mongoTemplate.indexOps(Store.class)
                .ensureIndex(new GeospatialIndex("location")
                        .typed(GEO_2DSPHERE));
        if (log.isDebugEnabled()) {
            log.debug("location index added for store entity");
        }
    }
}
