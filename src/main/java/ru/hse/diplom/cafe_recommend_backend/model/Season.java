package ru.hse.diplom.cafe_recommend_backend.model;

import java.time.LocalDateTime;

public enum Season {
    WINTER,
    SPRING,
    SUMMER,
    AUTUMN;

    public static Season getCurrentSeason() {
        var currentMonth = LocalDateTime.now().getMonth();
        return switch (currentMonth) {
            case DECEMBER, JANUARY, FEBRUARY -> WINTER;
            case MARCH, APRIL, MAY -> SPRING;
            case JUNE, JULY, AUGUST -> SUMMER;
            case SEPTEMBER, OCTOBER, NOVEMBER -> AUTUMN;
        };
    }
}
