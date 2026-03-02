package com.coupon.mgmt.dtos.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(
            HttpStatus status,
            String message,
            T data) {

        return new ApiResponse<>(
                true,
                status.value(),
                message,
                data
        );
    }

    public static <T> ApiResponse<T> failure(
            HttpStatus status,
            String message) {

        return new ApiResponse<>(
                false,
                status.value(),
                message,
                null
        );
    }
}