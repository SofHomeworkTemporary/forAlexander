package com.example.mvpmongo.handler;

import com.example.mvpmongo.exception.MongoException;
import com.example.mvpmongo.model.dto.other.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.NonNull;
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleInternalException(@NonNull Exception ex) {
        String bodyOfResponse = "Internal Server Error has occurred";
        ErrorResponse error = new ErrorResponse(bodyOfResponse);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


    @ExceptionHandler(MongoException.class)
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(@NonNull Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }


}
