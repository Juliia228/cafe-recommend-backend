package ru.hse.diplom.cafe_recommend_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class ErrorResponseDto {
    private String message;
    private Throwable cause;
}
