package com.nghiangong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(11, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(12, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(13, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(14, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(15, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(16, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(17, "You do not have permission", HttpStatus.FORBIDDEN),
    HOUSE_NOT_EXISTED(18, "House not existed", HttpStatus.BAD_REQUEST),
    ROOM_HAVING_CONTRACT(30, "Phòng này đang có hợp đồng.", HttpStatus.BAD_REQUEST),
    ROOM_ENOUGH_MEMBER(31, "Phòng đã có đủ người", HttpStatus.BAD_REQUEST),


    ELEC_NUMBER_NOT_ENTERED(40, "Chưa nhập số điện", HttpStatus.BAD_REQUEST),
    WATER_NUMBER_NOT_ENTERED(41, "Chưa nhập số nước", HttpStatus.BAD_REQUEST),
    ELEC_NUMBER_NOT_VALID(42, "Số điện nhập không hợp lệ", HttpStatus.BAD_REQUEST),
    WATER_NUMBER_NOT_VALID(43, "Số nước nhập không hợp lệ", HttpStatus.BAD_REQUEST),

    CONTRACT_INVALID(60, "Hợp đồng không hợp lệ", HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_EXISTED(61, "Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST),

    INVOICE_NOT_EXISTED(70, "Hóa đơn không tồn tại", HttpStatus.BAD_REQUEST),
    INVOICE_OF_MONTH_EXISTED(71, "Hóa đơn tháng đã tồn tại", HttpStatus.BAD_REQUEST),
    INVOICE_EXISTED(72, "Hóa đơn đã tồn tại", HttpStatus.BAD_REQUEST),

    TENANT_RENTING_NO_ROOM(80, "Không có phòng nào được đang thuê", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
