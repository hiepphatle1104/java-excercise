package com.swappie.swappie.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse<T> {
    private String status;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public static <T> AppResponse<T> success(String code, String message, T data) {
        return new AppResponse<>("SUCCESS", code, message, data, null);
    }

    public static <T> AppResponse<T> success(String code, T data) {
        return new AppResponse<>("SUCCESS", code, "Success", data, null);
    }

    public static <T> AppResponse<T> success(String code, String message) {
        return new AppResponse<>("SUCCESS", code, message, null, null);
    }

    public static <T> AppResponse<T> fail(String code, String message) {
        return new AppResponse<T>("FAILED", code, message, null, null);
    }

    public static <T> AppResponse<T> error(String code, String message, Map<String, String> errors) {
        return new AppResponse<T>("ERROR", code, message, null, errors);
    }

    public static <T> AppResponse<T> error(String code, String message) {
        return new AppResponse<T>("ERROR", code, message, null, null);
    }

    public static <T> AppResponse<T> error(String code, Map<String, String> errors) {
        return new AppResponse<T>("SUCCESS", code, "Error", null, errors);
    }
}
