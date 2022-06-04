package com.jumbo.map.service.impl;

import com.jumbo.map.MapTestApplication;
import com.jumbo.map.repository.StoreRepository;
import com.jumbo.map.service.MongoDBTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {MongoDBTestConfiguration.class, MapTestApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {MongoDBTestConfiguration.Initializer.class})
class StoreManagementServiceImplTest {

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    public void cleanDB(){
        storeRepository.deleteAll();
    }



}