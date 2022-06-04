package com.jumbo.map.service.impl;

import com.jumbo.map.MapTestApplication;
import com.jumbo.map.entity.Store;
import com.jumbo.map.repository.StoreRepository;
import com.jumbo.map.service.MongoDBTestConfiguration;
import com.jumbo.map.service.StoreManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {MongoDBTestConfiguration.class, MapTestApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {MongoDBTestConfiguration.Initializer.class})
class StoreManagementServiceImplIntegrationTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    StoreManagementService target;

    @BeforeEach
    public void cleanDB() {
        storeRepository.deleteAll();
    }


    /**
     * There are 10 stores and we run a query to find near stores around coordinate 3,3
     * page-size equals to 2 so there should be total 5 pages and each page contains two records
     */
    @Test
    void testNearLocation() {
        persistTenStoresWithDifferentLocations();

        Page<Store> result = target.loadAllNearBy("", new Point(3, 3),
                PageRequest.of(0, 2));
        assertFirstReturnedPage(result);


        result = target.loadAllNearBy("", new Point(3, 3),
                PageRequest.of(1, 2));
        assertSecondReturnedPage(result);


        result = target.loadAllNearBy("", new Point(3, 3),
                PageRequest.of(2, 2));
        assertThirdReturnedPage(result);

    }


    /**
     * There are 10 stores and we run a query to find near stores around coordinate 3,3
     * but we have another criteria that we want to return only locationType equals to citymarket instead of supermarket
     * , so there should be return only two records in just single page and they should be placed based on nearness
     */
    @Test
    void testNearLocationWithRSQL_both_supermarket_and_citymarket() {
        String cityMarket = "CitymarktPuP";
        persistTenStoresWithDifferentLocationsAndLocationType(cityMarket);

        Page<Store> result = target.loadAllNearBy("locationType==" + cityMarket, new Point(3, 3),
                PageRequest.of(0, 5));

        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(2.10, result.getContent().get(0).getLocation().getX(), 0.0005);
        assertEquals(2.10, result.getContent().get(0).getLocation().getY(), 0.0005);
        assertEquals(5.10, result.getContent().get(1).getLocation().getX(), 0.0005);
        assertEquals(5.10, result.getContent().get(1).getLocation().getY(), 0.0005);
    }

    @Test
    void testNearLocationWithRSQL_both_supermarket_and_citymarket_alongside_different_street() {

        String cityMarket = "CitymarktPuP";
        String admiraalStreet = "Admiraal";
        persistTenStoresWithDifferentLocationsAndLocationTypeAndStreet(cityMarket, admiraalStreet);

        //call target
        Page<Store> result = target.loadAllNearBy("locationType==" + cityMarket
                        + ";street==" + admiraalStreet
                , new Point(3, 3),
                PageRequest.of(0, 5));

        //assert result
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals(2.10, result.getContent().get(0).getLocation().getX(), 0.0005);
        assertEquals(2.10, result.getContent().get(0).getLocation().getY(), 0.0005);
        assertEquals(4.20, result.getContent().get(1).getLocation().getX(), 0.0005);
        assertEquals(4.20, result.getContent().get(1).getLocation().getY(), 0.0005);
        assertEquals(5.10, result.getContent().get(2).getLocation().getX(), 0.0005);
        assertEquals(5.10, result.getContent().get(2).getLocation().getY(), 0.0005);
    }

    /**
     * This test tries to find street based on regex and orderBy them ASCENDING with street field
     */
    @Test
    void testNearLocationWithRSQL_both_supermarket_and_citymarket_alongside_different_street_with_orderBy_street() {

        String cityMarket = "CitymarktPuP";
        String admiraalStreet = "Admiraal";
        persistTenStoresWithDifferentLocationsAndLocationTypeAndStreetRegex(cityMarket, admiraalStreet);

        Page<Store> result = target.loadAllNearBy("locationType==" + cityMarket
                        + ";street=re=" + admiraalStreet
                , new Point(3, 3),
                PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "street")));


        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals(4.20, result.getContent().get(0).getLocation().getX(), 0.0005);
        assertEquals(4.20, result.getContent().get(0).getLocation().getY(), 0.0005);
        assertEquals(2.10, result.getContent().get(1).getLocation().getX(), 0.0005);
        assertEquals(2.10, result.getContent().get(1).getLocation().getY(), 0.0005);
        assertEquals(5.10, result.getContent().get(2).getLocation().getX(), 0.0005);
        assertEquals(5.10, result.getContent().get(2).getLocation().getY(), 0.0005);
    }

    private void persistTenStoresWithDifferentLocationsAndLocationTypeAndStreetRegex(String cityMarket, String admiraalStreet) {

        String superMarket = "SupermarktPuP";
        String kerkStreet = "Kerkstraat";

        persistStore(new GeoJsonPoint(1.10, 1.10), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(1.20, 1.20), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(2.10, 2.10), cityMarket, admiraalStreet + "b");
        persistStore(new GeoJsonPoint(2.20, 2.20), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(3.10, 3.10), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(3.20, 3.20), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(4.10, 4.10), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(4.20, 4.20), cityMarket, admiraalStreet + "a");
        persistStore(new GeoJsonPoint(5.10, 5.10), cityMarket, admiraalStreet + "c");
        persistStore(new GeoJsonPoint(5.20, 5.20), superMarket, kerkStreet);
    }

    private void persistTenStoresWithDifferentLocations() {
        String locationType = "SupermarktPuP";
        String street = "Kerkstraat";
        persistStore(new GeoJsonPoint(1.10, 1.10), locationType, street);
        persistStore(new GeoJsonPoint(1.20, 1.20), locationType, street);
        persistStore(new GeoJsonPoint(2.10, 2.10), locationType, street);
        persistStore(new GeoJsonPoint(2.20, 2.20), locationType, street);
        persistStore(new GeoJsonPoint(3.10, 3.10), locationType, street);
        persistStore(new GeoJsonPoint(3.20, 3.20), locationType, street);
        persistStore(new GeoJsonPoint(4.10, 4.10), locationType, street);
        persistStore(new GeoJsonPoint(4.20, 4.20), locationType, street);
        persistStore(new GeoJsonPoint(5.10, 5.10), locationType, street);
        persistStore(new GeoJsonPoint(5.20, 5.20), locationType, street);
    }


    private void persistStore(GeoJsonPoint geoJsonPoint, String locationType, String street) {
        storeRepository.save(Store.builder()
                .location(geoJsonPoint)
                .id(UUID.randomUUID().toString())
                .locationType(locationType)
                .street(street)
                .build());
    }

    /**
     * Nearest result near coordinate 3,3 in first page should be 3.10,3.10 and 3.20,3.20
     *
     * @param result
     */
    private void assertFirstReturnedPage(Page<Store> result) {
        assertEquals(5, result.getTotalPages());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(3.10, result.getContent().get(0).getLocation().getX(), 0.0005);
        assertEquals(3.10, result.getContent().get(0).getLocation().getY(), 0.0005);
        assertEquals(3.20, result.getContent().get(1).getLocation().getX(), 0.0005);
        assertEquals(3.20, result.getContent().get(1).getLocation().getY(), 0.0005);
    }

    /**
     * Nearest result near coordinate 3,3 in second page should be 2.20,2.20 and 2.10,2.10
     *
     * @param result
     */
    private void assertSecondReturnedPage(Page<Store> result) {
        assertEquals(5, result.getTotalPages());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(2.20, result.getContent().get(0).getLocation().getX(), 0.0005);
        assertEquals(2.20, result.getContent().get(0).getLocation().getY(), 0.0005);
        assertEquals(2.10, result.getContent().get(1).getLocation().getX(), 0.0005);
        assertEquals(2.10, result.getContent().get(1).getLocation().getY(), 0.0005);
    }

    /**
     * Nearest result near coordinate 3,3 in third page should be 4.10,4.10 and 4.20,4.20
     *
     * @param result
     */
    private void assertThirdReturnedPage(Page<Store> result) {
        assertEquals(5, result.getTotalPages());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(4.10, result.getContent().get(0).getLocation().getX(), 0.0005);
        assertEquals(4.10, result.getContent().get(0).getLocation().getY(), 0.0005);
        assertEquals(4.20, result.getContent().get(1).getLocation().getX(), 0.0005);
        assertEquals(4.20, result.getContent().get(1).getLocation().getY(), 0.0005);
    }

    private void persistTenStoresWithDifferentLocationsAndLocationType(String cityMarket) {
        String superMarket = "SupermarktPuP";
        String street = "Kerkstraat";

        persistStore(new GeoJsonPoint(1.10, 1.10), superMarket, street);
        persistStore(new GeoJsonPoint(1.20, 1.20), superMarket, street);
        persistStore(new GeoJsonPoint(2.10, 2.10), cityMarket, street);
        persistStore(new GeoJsonPoint(2.20, 2.20), superMarket, street);
        persistStore(new GeoJsonPoint(3.10, 3.10), superMarket, street);
        persistStore(new GeoJsonPoint(3.20, 3.20), superMarket, street);
        persistStore(new GeoJsonPoint(4.10, 4.10), superMarket, street);
        persistStore(new GeoJsonPoint(4.20, 4.20), superMarket, street);
        persistStore(new GeoJsonPoint(5.10, 5.10), cityMarket, street);
        persistStore(new GeoJsonPoint(5.20, 5.20), superMarket, street);
    }

    private void persistTenStoresWithDifferentLocationsAndLocationTypeAndStreet(String cityMarket, String admiraalStreet) {
        String superMarket = "SupermarktPuP";
        String kerkStreet = "Kerkstraat";

        persistStore(new GeoJsonPoint(1.10, 1.10), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(1.20, 1.20), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(2.10, 2.10), cityMarket, admiraalStreet);
        persistStore(new GeoJsonPoint(2.20, 2.20), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(3.10, 3.10), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(3.20, 3.20), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(4.10, 4.10), superMarket, kerkStreet);
        persistStore(new GeoJsonPoint(4.20, 4.20), cityMarket, admiraalStreet);
        persistStore(new GeoJsonPoint(5.10, 5.10), cityMarket, admiraalStreet);
        persistStore(new GeoJsonPoint(5.20, 5.20), superMarket, kerkStreet);
    }
}