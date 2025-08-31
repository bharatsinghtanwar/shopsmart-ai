package com.shopsmart.backend.dto;


public class ApiResponse<T> {
    private T data;
    private ErrorResponse error;

    public ApiResponse(T data, ErrorResponse error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(null, new ErrorResponse(message));
    }

    public T getData() {
        return data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
