package com.nghiangong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Lỗi chưa xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(11, "Khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_EXISTED(12, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(13, "Tên người dùng phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(14, "Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(15, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(16, "Lỗi xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(17, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    WRONG_PASSWORD(18, "Mật khẩu không đúng", HttpStatus.BAD_REQUEST),

    HOUSE_NOT_EXISTED(21, "Tòa nhà không tồn tại", HttpStatus.BAD_REQUEST),
    HOUSE_HAVING_OCCUPIED_ROOM(22, "Tòa nhà có phòng đang thuê", HttpStatus.BAD_REQUEST),
    NOT_DELETE_HOUSE(23, "Không thể xóa. Tòa nhà đang có liên quan đến nhiều phòng", HttpStatus.BAD_REQUEST),
    HOUSE_INACTIVE(24, "Tòa nhà đang ngưng cho thuê", HttpStatus.BAD_REQUEST),

    ROOM_HAVING_CONTRACT(30, "Phòng này đang có hợp đồng.", HttpStatus.BAD_REQUEST),
    ROOM_ENOUGH_MEMBER(31, "Phòng đã có đủ người", HttpStatus.BAD_REQUEST),
    ROOM_INACTIVE(32, "Phòng đang ngưng cho thuê", HttpStatus.BAD_REQUEST),
    ROOM_NOT_EXISTED(33, "Phòng không tồn tại", HttpStatus.BAD_REQUEST),
    NOT_DELETE_ROOM(34, "Không thể xóa. Phòng đang có liên quan đến nhiều hợp đồng", HttpStatus.BAD_REQUEST),

    ELEC_NUMBER_NOT_ENTERED(40, "Chưa nhập số điện", HttpStatus.BAD_REQUEST),
    WATER_NUMBER_NOT_ENTERED(41, "Chưa nhập số nước", HttpStatus.BAD_REQUEST),
    ELEC_NUMBER_NOT_VALID(42, "Số điện nhập không hợp lệ", HttpStatus.BAD_REQUEST),
    WATER_NUMBER_NOT_VALID(43, "Số nước nhập không hợp lệ", HttpStatus.BAD_REQUEST),
    START_ELEC_NUMBER_NOT_VALID(44, "Số điện đầu không hợp lệ", HttpStatus.BAD_REQUEST),
    START_WATER_NUMBER_NOT_VALID(45, "Số nước đầu không hợp lệ", HttpStatus.BAD_REQUEST),
    END_ELEC_NUMBER_NOT_VALID(46, "Số điện cuối không hợp lệ", HttpStatus.BAD_REQUEST),
    END_WATER_NUMBER_NOT_VALID(47, "Số nước cuối không hợp lệ", HttpStatus.BAD_REQUEST),

    CONTRACT_INVALID(60, "Hợp đồng không hợp lệ", HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_EXISTED(61, "Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_EXPIRED(62, "Hợp đồng chưa hết hạn", HttpStatus.BAD_REQUEST),
    CONTRACT_NO_STATUS(63, "Hợp đồng không có trạng thái", HttpStatus.INTERNAL_SERVER_ERROR),
    EXPIRED_CONTRACT_NOT_CHANGE_END_DATE(64, "Hợp đồng đã kết thúc. Không thể thay đổi ngày kết thúc.", HttpStatus.BAD_REQUEST),
    NOT_DELETE_CONTRACT(65, "Không thể xóa. Hợp đồng đang có liên quan đến nhiều hóa đơn.", HttpStatus.BAD_REQUEST),
    CONTRACT_PERIOD_INTERSECTING(66, "Thời gian của hợp đồng giao với hợp đồng khác", HttpStatus.BAD_REQUEST),
    CONTRACT_START_DATE_LESS_THAN_END_DATE(67, "Ngày kết thúc phải muộn hơn ngày bắt đầu", HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_ENTER_START_ELEC_RECORD(68, "Chưa nhập số điện đầu", HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_ENTER_START_WATER_RECORD(69, "Chưa nhập số nước đầu", HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_CONTAIN_MONTH(90, "Hợp đồng không bao gồm tháng này", HttpStatus.BAD_REQUEST),

    INVOICE_NOT_EXISTED(70, "Hóa đơn không tồn tại", HttpStatus.BAD_REQUEST),
    INVOICE_OF_MONTH_EXISTED(71, "Hóa đơn tháng đã tồn tại", HttpStatus.BAD_REQUEST),
    INVOICE_EXISTED(72, "Hóa đơn đã tồn tại", HttpStatus.BAD_REQUEST),
    NOT_DELETE_INVOICE(73, "Không thể xóa. Hóa đơn đã thanh toán", HttpStatus.BAD_REQUEST),

    TENANT_RENTING_NO_ROOM(80, "Không có phòng nào được đang thuê", HttpStatus.BAD_REQUEST),
    TENANT_NOT_EXIST(81, "Khách thuê không tồn tại", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
