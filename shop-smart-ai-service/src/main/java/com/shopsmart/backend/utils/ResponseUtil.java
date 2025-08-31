package com.shopsmart.backend.utils;


import com.shopsmart.backend.dto.ApiResponse;

public class ResponseUtil {

    private ResponseUtil() {
        // Prevent instantiation
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, new ApiResponse.ErrorResponse(message));
    }
}
