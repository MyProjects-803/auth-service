package com.job.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentExceptions(IllegalArgumentException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<?> handleInvalidCredentialException(InvalidCredentialException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    private ResponseEntity<?> buildResponse(HttpStatus status, String msg){
        return ResponseEntity.status(status)
                .body(Map.of("msg", msg,
                        "timestamp", System.currentTimeMillis()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String msg, String path){
        ErrorResponse errorResponse = new ErrorResponse(Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                msg,
                path);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

