package com.jumbo.customerpanel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Act as a general out dto which handles single and list data types
 * it will help the consumer to have clear expectations from our API.
 * It is returned when HttpStatus is 2xx, also bringing this class helps to
 * cover bushiness logic error in better ways (we dont need different https statuses for each
 * custom errors- handled with success field)
 *
 * @param <T>
 */
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