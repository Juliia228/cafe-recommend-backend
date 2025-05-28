package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PASSWORD_REGEXP;
import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PHONE_REGEXP;

@Data
public class ResetPasswordRequestDto {
    @NotBlank
    @Pattern(regexp = PHONE_REGEXP)
    private String phone;

    @NotBlank
    private String keyWord;

    @NotBlank
    @Pattern(regexp = PASSWORD_REGEXP)
    private String newPassword;

}
