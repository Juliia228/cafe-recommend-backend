package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PHONE_REGEXP;

@Getter
public class ResetPasswordRequestDto {
    @NotBlank
    @Pattern(regexp = PHONE_REGEXP)
    private String phone;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

}
