package com.hublocal.board.handler.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){

        List errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String > errorMap = new HashMap<>();
                    try {
                        errorMap.put("fieldName", fieldError.getField());
                        errorMap.put("value", fieldError.getDefaultMessage() + ", provided value is: " + fieldError.getRejectedValue());
                    } catch (NullPointerException e) {
                        errorMap.put(fieldError.getField(), fieldError.getDefaultMessage() + ", provided empty value");
                    }
                    errorMap.put("code", fieldError.getCode());
                    return errorMap;
                }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<NotFoundResponse> handleNotFound(NotFoundException exception, HttpServletRequest httpServletRequest){

        NotFoundResponse notFoundResponse = new NotFoundResponse(404, "Resource not found at " +
                "desired path", httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(notFoundResponse);
    }

//    @ExceptionHandler(NoResourceFoundException.class)
//    ResponseEntity handleNotFound(NoResourceFoundException exception){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
//                .body(List.of(exception.getStatusCode(), exception.getCause(), exception.getResourcePath()));
//    }

    @ExceptionHandler(CustomException.class)
    ResponseEntity<CustomExceptionResponse> handleCustom(CustomException exception){

        CustomExceptionResponse response = new CustomExceptionResponse(400, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<CustomExceptionResponse> handleNoResourceFoundException(NoHandlerFoundException exception, HttpServletRequest httpServletRequest) {
        log.error("Resource not found", exception);
//        CustomErrorResource resource = CustomErrorResource.builder()
//                .timestamp(Instant.now().toString())
//                .status(exception.getStatusCode().value())
//                .error(exception.getMessage())
//                .path(exception.getResourcePath())
//                .build();

        CustomExceptionResponse response = new CustomExceptionResponse(404, "Page not found: " + httpServletRequest.getRequestURI());
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(response);
    }

    @Data
    @Builder
    public static class CustomErrorResource {
        private String timestamp;
        private int status;
        private String error;
        private String path;
    }
}