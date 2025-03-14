package org.compass.mseventmanagerapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(LocalDateTime timestamp, int status, String message) {
        this(timestamp, status, message, null);
    }
}
