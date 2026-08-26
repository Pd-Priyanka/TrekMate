package com.trekmate.exception;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    ResponseEntity<ApiError> conflict(ConflictException e, HttpServletRequest r) {
        return response(HttpStatus.CONFLICT, e.getMessage(), r);
    }

    @ExceptionHandler(UnauthorizedException.class)
    ResponseEntity<ApiError> unauthorized(UnauthorizedException e, HttpServletRequest r) {
        return response(HttpStatus.UNAUTHORIZED, e.getMessage(), r);
    }

    @ExceptionHandler(ForbiddenException.class)
    ResponseEntity<ApiError> forbidden(ForbiddenException e, HttpServletRequest r) {
        return response(HttpStatus.FORBIDDEN, e.getMessage(), r);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiError> notFound(NotFoundException e, HttpServletRequest r) {
        return response(HttpStatus.NOT_FOUND, e.getMessage(), r);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(IllegalArgumentException e, HttpServletRequest r) {
        return response(HttpStatus.BAD_REQUEST, e.getMessage(), r);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException e, HttpServletRequest r) {
        String message = e.getBindingResult().getFieldErrors().stream().findFirst().map(error -> error.getField() + ": " + error.getDefaultMessage()).orElse("Validation failed.");
        return response(HttpStatus.BAD_REQUEST, message, r);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiError> responseStatus(ResponseStatusException e, HttpServletRequest r) {
        return response(HttpStatus.valueOf(e.getStatusCode().value()), e.getReason(), r);
    }

    @ExceptionHandler(RestClientResponseException.class)
    ResponseEntity<ApiError> weatherApi(RestClientResponseException e, HttpServletRequest r) {
        return response(HttpStatus.valueOf(e.getStatusCode().value()), "OpenWeather request failed: " + e.getStatusText(), r);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiError> dataIntegrity(DataIntegrityViolationException e, HttpServletRequest r) {
        return response(HttpStatus.CONFLICT, "The request conflicts with existing data.", r);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI()));
    }
}
