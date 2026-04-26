package com.nie.secondhub.util;

import com.nie.secondhub.common.response.ApiResponse;

import java.time.LocalDateTime;

public class Result {
    private Integer code;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public Result() {
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static Result success(String message) {
        return new Result(0, message, null);
    }

    public static Result success(String message, Object data) {
        return new Result(0, message, data);
    }

    public static Result fail(Integer code, String message) {
        return new Result(code, message, null);
    }

    public static Result error(String message) {
        return new Result(500, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ApiResponse<Object> toApiResponse() {
        return ApiResponse.builder()
                .code(this.code)
                .message(this.message)
                .data(this.data)
                .timestamp(this.timestamp)
                .build();
    }
}