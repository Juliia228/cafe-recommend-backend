package ru.hse.diplom.cafe_recommend_backend.service;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Map;
import java.util.UUID;

public class Utils {
    public static <T> T onNull(T object, T defaultValue) {
        return object == null ? defaultValue : object;
    }

    public static RealVector getVector(Map<UUID, Integer> map) {
        return new ArrayRealVector(map.values().stream().mapToDouble(d -> d).toArray());
    }

    public static double getCosineSimilarity(RealVector v1, RealVector v2) {
        return v1.dotProduct(v2) / (v1.getNorm() * v2.getNorm());
    }

    public static <T> T getValueOfDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
