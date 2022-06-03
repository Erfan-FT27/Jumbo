package com.jumbo.customerpanel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorModel {

    @JsonProperty("errorReason")
    private String errorReason;

    @JsonProperty("errorReasons")
    private List<String> errorReasons;

    private ErrorType errorType;

    public ErrorModel(String errorReason) {
        this.errorReason = errorReason;
    }

    /**
     * only needs in json
     *
     * @return
     */
    public ErrorType getErrorType() {
        if (StringUtils.hasText(errorReason)) return ErrorType.SINGLE;
        if (errorReasons != null) return ErrorType.MULTIPLE;
        return errorType;
    }

    private enum ErrorType {
        SINGLE, MULTIPLE;
    }
}
