package com.jumbo.customerpanel.controller;

import com.jumbo.customerpanel.model.ActionResult;
import com.jumbo.customerpanel.model.ErrorModel;
import com.jumbo.customerpanel.utils.MessageTranslatorUtil;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

/**
 * Handle application exceptions
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * Handle binding exception that raised from javax validation
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel handleBindingException(BindException ex) {
        log.error("received invalid parameter [{}] ", ex.getMessage());
        return ErrorModel.builder()
                .errorReasons(buildErrorReasonInCaseOfBindingFailure(ex.getBindingResult()))
                .build();
    }

    /**
     * Handle MongoDB exceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MongoException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorModel handleMongoDBException(MongoException ex) {
        log.error("DB exception : [{}]", ex.getMessage());
        return ErrorModel.builder()
                .errorReason(ex.getMessage())
                .build();
    }

    /**
     * Handle concurrent exception that will raised in detailed search service when
     * queue capacity is fulled
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({RejectedExecutionException.class})
    @ResponseStatus(HttpStatus.OK)
    public ActionResult<String> handlingConcurrentExecutionException(RejectedExecutionException ex) {
        log.error("execution exception occurred : [{}]", ex.getMessage());
        return ActionResult.<String>builder()
                .message(MessageTranslatorUtil.getText("global.exception.handler.rejected"))
                .success(Boolean.FALSE)
                .build();
    }

    /**
     * Receive binding result error messages and translate with MessageTranslatorUtil
     * to return more suitable result
     *
     * @param bindingResult
     * @return
     */
    private List<String> buildErrorReasonInCaseOfBindingFailure(BindingResult bindingResult) {
        if (log.isDebugEnabled())
            log.debug("binding failure with reason [{}]", bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        return bindingResult.getFieldErrors().stream()
                .map(br -> {
                    List<Object> args = Arrays.stream(Objects.requireNonNull(br.getArguments())).collect(Collectors.toList());
                    args.remove(0);
                    return MessageTranslatorUtil.getText(br.getDefaultMessage(), args.toArray(new Object[0]));
                })
                .collect(Collectors.toList());
    }

}