package com.jumbo.customerpanel.model;

import com.jumbo.customerpanel.Constant;
import com.jumbo.customerpanel.dto.StoreOutDto;
import com.jumbo.map.entity.Store;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jumbo.customerpanel.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

class PageDataTest {


    @Test
    void testConvert_when_pageIsNUll() {
        PageData<Object> pageData = PageData.convert(null, null);
        assertNull(pageData.getItems());
    }

    @Test
    void testConvert_when_functionIsNUll() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> PageData.convert(new PageImpl<>(Collections.emptyList()), null));
        assertNotNull(illegalArgumentException);
    }

    /**
     * generate ten records and test result for each item
     */
    @Test
    void testConvert_when_everyThingIsOk() {

        final SplittableRandom random = new SplittableRandom();

        final String street = "street";
        List<Store> tenStores = generateTenStores(random, street);

        PageData<StoreOutDto> result = PageData.convert(new PageImpl<>(tenStores),
                (Store s) -> StoreOutDto.construct().map(s));

        assertEquals(10, result.getItems().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(10, result.getTotalElements());

        result.getItems().forEach(storeOutDto -> {
            assertEquals(street, storeOutDto.getStreet());
            assertTrue(LONGITUDE_MIN_SIZE <= storeOutDto.getLongitude() && storeOutDto.getLongitude() <= LONGITUDE_MAX_SIZE);
            assertTrue(LATITUDE_MIN_SIZE <= storeOutDto.getLatitude() && storeOutDto.getLatitude() <= LATITUDE_MAX_SIZE);
        });

    }

    @NotNull
    private List<Store> generateTenStores(SplittableRandom random, String street) {
        return IntStream.range(0, 10)
                .mapToObj(value -> Store.builder()
                        .location(generateRandomValidLocation(random))
                        .street(street)
                        .build())
                .collect(Collectors.toList());
    }

    private GeoJsonPoint generateRandomValidLocation(SplittableRandom random) {
        return new GeoJsonPoint(random.nextDouble(LONGITUDE_MIN_SIZE
                , LONGITUDE_MAX_SIZE), random.nextDouble(LATITUDE_MIN_SIZE,
                Constant.LATITUDE_MAX_SIZE));
    }
}