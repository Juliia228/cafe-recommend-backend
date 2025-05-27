package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PHONE_REGEXP;

@Getter
public class ResetPasswordRequestDto {
    @NotNull
    @Pattern(regexp = PHONE_REGEXP)
    private String phone;

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;

}
