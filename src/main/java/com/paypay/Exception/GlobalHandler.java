package com.paypay.Exception;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.paypay.dto.Response.Response;
import com.paypay.dto.Response.ResponseData;

@ControllerAdvice
public class GlobalHandler {
    private Response response;
    private Map<String, Object> errors;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response restControllerErrorHandler(final Exception ex) {
        response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), MDC.get("X-B3-TraceId").toString(), null);
        return response;
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<?> nullAttribute(BadRequestException e) {
        ResponseData responseData = new ResponseData();
        responseData.setStatus(false);
        responseData.setMessage(e.getMessage());
        response = new Response(HttpStatus.BAD_REQUEST.value(), "Failed", responseData);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        errors = new HashMap<>();

        exception.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        response = new Response(HttpStatus.BAD_REQUEST.value(), exception.getFieldError().getDefaultMessage(), errors);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
