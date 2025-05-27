package ru.hse.diplom.cafe_recommend_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAnyException(Exception e, HttpServletRequest request) {
        log.error(request.getRequestURL() + " raised " + e);
        return ResponseEntity.badRequest().body(ErrorResponseDto.of("ERROR: " + e.getMessage(), e.getCause()));
    }

}
