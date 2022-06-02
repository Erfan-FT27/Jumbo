package com.jumbo.map.service;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.jumbo.map.MapTestApplication;
import com.jumbo.map.dto.StoreListDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = MapTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreInfoReaderTest {

    @Autowired
    StoreInfoReader reader;

    @Test
    void testReadMethod_When_fileIsNotPresent() {
        FileNotFoundException notFoundException = assertThrows(FileNotFoundException.class,
                () -> reader
                        .readFromResource("classpath:/mapinfo/absent-stores.json"));
        assertNotNull(notFoundException);

    }


    @Test
    @SneakyThrows
    void testReadMethod_When_fileIsPresentButIsEmpty() {
        StoreListDto list = reader.readFromResource("classpath:/mapinfo/emptystores.json");
        assertNull(list.getStores());
    }

    @Test
    void testReadMethod_When_fileIsPresentAndIsUnparsable() {
        MismatchedInputException inputException = assertThrows(MismatchedInputException.class,
                () -> reader
                        .readFromResource("classpath:/mapinfo/unparsablestores.json"));
        assertNotNull(inputException);
    }

    @Test
    @SneakyThrows
    void testReadMethod_When_fileIsPresentAndStreetFieldIsNotPresent() {
        StoreListDto list = reader.readFromResource("classpath:/mapinfo/streetfieldmissingstores.json");

        assertFalse(list.getStores().isEmpty());
        assertEquals(1, list.getStores().size());
        assertNull(list.getStores().get(0).getStreet());
    }

    @Test
    @SneakyThrows
    void testReadMethod_When_fileIsPresentAndHasValidInfo() {
        StoreListDto list = reader.read();

        assertFalse(list.getStores().isEmpty());
        assertEquals(1, list.getStores().size());
        assertEquals("Kerkstraat", list.getStores().get(0).getStreet());

    }
}
