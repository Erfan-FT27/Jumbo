package com.jumbo.customerpanel.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActionResult<T> {

    @JsonProperty("dataType")
    private ActionDataType actionDataType;
    private T data;
    private PageData<T> pageData;
    private boolean success;
    private String message;

    /**
     * only needs in json
     */
    public ActionDataType getActionDataType() {
        if (data != null && data != "") return ActionDataType.SINGLE;
        if (pageData != null) return ActionDataType.LIST;
        return actionDataType;
    }


    public enum ActionDataType {
        SINGLE, LIST;
    }
}