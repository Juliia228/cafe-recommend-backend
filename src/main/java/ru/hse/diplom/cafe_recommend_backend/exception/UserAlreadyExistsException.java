package ru.hse.diplom.cafe_recommend_backend.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String phone) {
        super("User with phone=" + phone + " already exists");
    }
}
