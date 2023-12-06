package com.kanvan.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException e) {
        log.error("SignatureException, {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException e) {
        log.error("MalformedJwtException, {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("ExpiredJwtException, {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException, {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
