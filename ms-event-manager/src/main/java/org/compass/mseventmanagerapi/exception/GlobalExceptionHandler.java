package org.compass.mseventmanagerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EventHasTicketsException.class)
    public ResponseEntity<Object> handleEventHasTickets(EventHasTicketsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("{\"error\": \"" + ex.getMessage() + "\"}");
    }
}
