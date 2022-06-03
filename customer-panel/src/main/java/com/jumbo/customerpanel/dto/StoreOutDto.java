package com.jumbo.customerpanel.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.jumbo.customerpanel.config.EntityToDtoMapper;
import com.jumbo.customerpanel.config.View.Detailed;
import com.jumbo.customerpanel.config.View.Simple;
import com.jumbo.map.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(staticName = "construct")
public class StoreOutDto implements EntityToDtoMapper<Store, StoreOutDto> {

    @JsonView(Simple.class)
    private String id;

    @JsonView(Simple.class)
    private double longitude;
    @JsonView(Simple.class)
    private double latitude;
    @JsonView(Simple.class)
    private String locationType;
    @JsonView(Simple.class)
    private String todayOpen;
    @JsonView(Simple.class)
    private String todayClose;

    @JsonView(Detailed.class)
    private String city;
    @JsonView(Detailed.class)
    private String postalCode;
    @JsonView(Detailed.class)
    private String street;
    @JsonView(Detailed.class)
    private String street2;
    @JsonView(Detailed.class)
    private String street3;
    @JsonView(Detailed.class)
    private String addressName;

    @JsonView(Detailed.class)
    private String complexNumber;
    @JsonView(Detailed.class)
    private boolean showWarningMessage;
    @JsonView(Detailed.class)
    private boolean collectionPoint;
    @JsonView(Detailed.class)
    private String sapStoreID;


    @Override
    public StoreOutDto map(Store entity) {
        return StoreOutDto.builder()
                .id(entity.getId())
                .city(entity.getCity())
                .postalCode(entity.getPostalCode())
                .street(entity.getStreet())
                .street2(entity.getStreet2())
                .street3(entity.getStreet3())
                .addressName(entity.getAddressName())
                .longitude(entity.getLocation().getX())
                .latitude(entity.getLocation().getY())
                .locationType(entity.getLocationType())
                .todayOpen(entity.getTodayOpen())
                .todayClose(entity.getTodayClose())
                .complexNumber(entity.getComplexNumber())
                .showWarningMessage(entity.isShowWarningMessage())
                .collectionPoint(entity.isCollectionPoint())
                .sapStoreID(entity.getSapStoreID())
                .build();
    }
}
