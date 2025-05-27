package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PASSWORD_REGEXP;
import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PHONE_REGEXP;

@Getter
public class AuthRequestDto {
    @Pattern(regexp = PHONE_REGEXP)
    @NotBlank(message = "login is required")
    private String login;

    @Pattern(regexp = PASSWORD_REGEXP,
            message = "password must be at least 8 characters long, contain at least 1 digit, 1 uppercase letter, 1 lowercase letter and 1 special character")
    @NotBlank(message = "password is required")
    private String password;
}
