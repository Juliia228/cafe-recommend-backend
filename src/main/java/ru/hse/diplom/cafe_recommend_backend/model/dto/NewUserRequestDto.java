package ru.hse.diplom.cafe_recommend_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PASSWORD_REGEXP;
import static ru.hse.diplom.cafe_recommend_backend.model.Constants.PHONE_REGEXP;

@Data
public class NewUserRequestDto {
    @Pattern(regexp = PHONE_REGEXP)
    @NotBlank(message = "phone is required")
    private String phone;

    @Pattern(regexp = PASSWORD_REGEXP, message = "password must be at least 8 characters long, contain at least 1 digit, 1 uppercase letter, 1 lowercase letter and 1 special character")
    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

//    private Integer loyaltyDiscount;

    private String[] roles;
}
