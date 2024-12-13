package com.nghiangong.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nghiangong.exception.ErrorCode;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private int code = 10;

    private String message;
    private T result;

    public ApiResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ApiResponse(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        if (message != null) this.message = message;
        else this.message = errorCode.getMessage();
    }
}
