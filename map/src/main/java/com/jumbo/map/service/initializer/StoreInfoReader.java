package com.jumbo.map.service.initializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumbo.map.model.Stores;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


/**
 * StoreReader will load stores info from mapinfo/stores.json in resource section
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StoreInfoReader {


    private static final String RESOURCE_LOCATION = "classpath:/mapinfo/stores.json";
    private static final ObjectMapper mapper;
    private final ResourceLoader resourceLoader;

    static {
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @SneakyThrows
    public Stores read() {
        if (log.isDebugEnabled()) {
            log.debug("Start reading from source");
        }
        return readFromResource(RESOURCE_LOCATION);
    }

    Stores readFromResource(String resourcePath) throws java.io.IOException {
        return mapper.readValue(resourceLoader.getResource(resourcePath).getInputStream(), Stores.class);
    }

}
