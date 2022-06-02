package com.jumbo.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreListDto {
    private List<StoreDto> stores;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class StoreDto {

        private String city;
        private String postalCode;
        private String street;
        private String street2;
        private String street3;
        private String addressName;
        private String uuid;
        private String longitude;
        private String latitude;
        private String complexNumber;
        private Boolean showWarningMessage;
        //TODO date format only hh:mm
        private String todayOpen;
        private String todayClose;

        private String locationType;
        private Boolean collectionPoint;
        private String sapStoreID;
    }

}
