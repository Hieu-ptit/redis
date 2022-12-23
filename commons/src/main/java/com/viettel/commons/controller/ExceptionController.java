package com.viettel.commons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.commons.exception.BusinessErrorCode;
import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.exception.FieldViolation;
import com.viettel.commons.exception.ValidateException;
import com.viettel.commons.model.response.Response;
import com.viettel.commons.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(BusinessException.class)
    protected void handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, e.getErrorCode(), request, response);
    }

    @ExceptionHandler(Exception.class)
    protected void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, ErrorCode.INTERNAL_SERVER_ERROR, request, response);
    }

    @ExceptionHandler(BindException.class)
    protected void handleBindException(BindException e, HttpServletRequest request,
                                                         HttpServletResponse response) throws IOException {
        var fieldViolations = e.getBindingResult().getAllErrors().stream()
                .map(error -> new FieldViolation(((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        handleInvalidParams(e, fieldViolations, request, response);
    }

    @ExceptionHandler(ValidateException.class)
    protected void handleValidateException(ValidateException e, HttpServletRequest request, HttpServletResponse response) throws  IOException {
        handleInvalidParams(e, e.getFields(), request, response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected void handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request, HttpServletResponse response) throws  IOException {
        handle(e, ErrorCode.INVALID_PARAMETERS, request, response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected void handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request, HttpServletResponse response) throws  IOException {
        var fieldViolations = List.of(new FieldViolation(e.getParameterName(), e.getMessage()));
        handleInvalidParams(e, fieldViolations, request, response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var fieldViolations = List.of(new FieldViolation(e.getName(), e.getMessage()));
        handleInvalidParams(e, fieldViolations, request, response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, ErrorCode.FORBIDDEN, request, response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, ErrorCode.UNAUTHORIZED, request, response);
    }

    private void handle(Exception e, BusinessErrorCode errorCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var errorResponse = Response.ofFailed(errorCode, e.getMessage());
        log.error("{}", errorResponse, e);
        writeResponse(response, errorCode.getHttpStatus(), errorResponse);
    }

    private void handleInvalidParams(Exception e, List<FieldViolation> fieldViolations, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var errorResponse = Response.ofFailed(ErrorCode.INVALID_PARAMETERS, e.getMessage(), fieldViolations);;
        log.error("{}", errorResponse, e);
        writeResponse(response, ErrorCode.INVALID_PARAMETERS.getHttpStatus(), errorResponse);
    }

    private void writeResponse(HttpServletResponse servletResponse, int httpStatus, Response errorResponse) throws IOException {
        servletResponse.setStatus(httpStatus);
        servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] body = objectMapper.writeValueAsBytes(errorResponse);
        servletResponse.setContentLength(body.length);
        servletResponse.getOutputStream().write(body);
    }
}
