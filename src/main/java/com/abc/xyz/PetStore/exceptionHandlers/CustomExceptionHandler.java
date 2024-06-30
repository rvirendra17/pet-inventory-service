package com.abc.xyz.PetStore.exceptionHandlers;

import com.abc.xyz.PetStore.constants.Constants;
import com.abc.xyz.PetStore.constants.ExceptionConstants;
import com.abc.xyz.PetStore.exceptions.PetException;
import com.abc.xyz.PetStore.models.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Exception in validating request payload: {}", transactionId, ex);

        Set<String> errors = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toSet());
        errors.addAll(ex.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toSet()));
        String errorString = errors.stream().collect(Collectors.joining(","));
        String errorMessage = Constants.INVALID_MISSING_PARAMETERS + errorString;

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = prepareErrorResponse(httpStatus, errorMessage, request);
        ResponseEntity<Object> errorResponseResponseEntity = new ResponseEntity<>(errorResponse, httpStatus);

        logExceptionResponse(transactionId, errorResponseResponseEntity);
        return errorResponseResponseEntity;
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Resource not found exception: {}", transactionId, ex);
        return processException(ex, status, request, transactionId);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Media type not acceptable exception: {}", transactionId, ex);
        return processException(ex, status, request, transactionId);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Media type not supported exception: {}", transactionId, ex);
        return processException(ex, status, request, transactionId);

    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Missing path variable exception: {}", transactionId, ex);
        return processException(ex, status, request, transactionId);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Missing servlet request parameter exception: {}", transactionId, ex);
        return processException(ex, status, request, transactionId);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String transactionId = getTransactionId(request);
        log.error("transactionId: {}, Http request not supported exception: {}", transactionId, ex);
        return processException(ex, status, request, transactionId);
    }


    private ResponseEntity<Object> processException(Exception exception, HttpStatusCode status, WebRequest request, String transactionId) {
        HttpStatus httpStatus = HttpStatus.valueOf(status.value());
        String errorMessage = getExceptionMessage(exception, request);
        ErrorResponse errorResponse = prepareErrorResponse(HttpStatus.valueOf(status.value()), errorMessage, request);
        ResponseEntity<Object> errorResponseResponseEntity = new ResponseEntity<>(errorResponse, httpStatus);
        logExceptionResponse(transactionId, errorResponseResponseEntity);
        return errorResponseResponseEntity;
    }

    private String getExceptionMessage(Exception exception, WebRequest request) {

        String requestUri = getRequestURI(request);

        if (exception instanceof HttpMediaTypeNotSupportedException) {
            return ExceptionConstants.EXCEPTION_HTTP_MEDIA_TYPE_NOT_SUPPORTED;
        } else if (exception instanceof HttpMediaTypeNotAcceptableException) {
            return ExceptionConstants.EXCEPTION_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE;
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return ExceptionConstants.EXCEPTION_HTTP_REQUEST_METHOD_NOT_SUPPORTED + requestUri;
        } else if (exception instanceof NoResourceFoundException) {
            return ExceptionConstants.EXCEPTION_NO_RESOURCE_FOUND + requestUri;
        } else if (exception instanceof MissingPathVariableException) {
            return ExceptionConstants.EXCEPTION_MISSING_PATH_VARIABLE;
        } else if (exception instanceof MissingServletRequestParameterException) {
            return ExceptionConstants.EXCEPTION_MISSING_SERVLET_REQUEST_PARAMETER + requestUri;
        } else {
            return ExceptionConstants.EXCEPTION_INTERNAL_SERVER_ERROR;
        }
    }

    @ExceptionHandler({PetException.class})
    public ResponseEntity<ErrorResponse> handlePetException(PetException ex, WebRequest webRequest) {
        String transactionId = getTransactionId(webRequest);
        log.error("transactionId: {}, Exception in processing Pet request: {}", transactionId, ex);
        ErrorResponse errorResponse = prepareErrorResponse(ex.getHttpStatus(), ex.getMessage(), webRequest);
        ResponseEntity<ErrorResponse> errorResponseResponseEntity = new ResponseEntity<>(errorResponse, ex.getHttpStatus());
        logExceptionResponse(transactionId, errorResponseResponseEntity);
        return errorResponseResponseEntity;
    }


    private String getTransactionId(WebRequest request) {
        return request.getHeader(Constants.TRANSACTION_ID);
    }

    private ErrorResponse prepareErrorResponse(HttpStatus httpStatus, String message, WebRequest request) {
        return new ErrorResponse(httpStatus.name(), httpStatus.value(), message, Constants.RESPONSE_TYPE_DEFINITION, getTransactionId(request), getRequestURI(request));
    }

    private String getRequestURI(WebRequest webRequest) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
        return httpServletRequest.getRequestURI();
    }

    private <T> void logExceptionResponse(String transactionId, ResponseEntity<T> errorResponseResponseEntity) {
        log.info("transactionId: {}, Exception Response to the consumer: {}", transactionId, errorResponseResponseEntity);
    }

}
