package com.jumbo.map.entity;

import com.jumbo.map.model.Stores.StoreModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "stores")
public class Store {

    @Id
    private String id;

    private String city;
    private String postalCode;
    private String street;
    private String street2;
    private String street3;
    private String addressName;

    @GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private String locationType;

    private String todayOpen;
    private String todayClose;

    private String complexNumber;
    private boolean showWarningMessage;
    private boolean collectionPoint;
    private String sapStoreID;


    public static Store of(StoreModel model) {
        return Store.builder()
                .id(model.getUuid())
                .city(model.getCity())
                .postalCode(model.getPostalCode())
                .street(model.getStreet())
                .street2(model.getStreet2())
                .street3(model.getStreet3())
                .addressName(model.getAddressName())
                .location(new GeoJsonPoint(Double.parseDouble(model.getLongitude())
                        , Double.parseDouble(model.getLatitude())))
                .locationType(model.getLocationType())
                .todayOpen(model.getTodayOpen())
                .todayClose(model.getTodayClose())
                .complexNumber(model.getComplexNumber())
                .showWarningMessage(model.isShowWarningMessage())
                .collectionPoint(model.isCollectionPoint())
                .sapStoreID(model.getSapStoreID())
                .build();
    }

}
