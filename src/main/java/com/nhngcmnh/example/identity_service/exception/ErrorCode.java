package com.nhngcmnh.example.identity_service.exception;
import org.springframework.http.HttpStatus;
public enum ErrorCode {
    USER_EXISTED(1001, "Tên người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1002, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003,"Mật khẩu không đủ 8 ký tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND(1004, "Không tìm thấy tài nguyên", HttpStatus.NOT_FOUND),
    UNAUTHENTICATION(4010, "Chưa xác thực hoặc token không hợp lệ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZATION(4030, "Không có quyền truy cập tài nguyên", HttpStatus.FORBIDDEN),
    INVALID_KEY(9999,"Lỗi không xác định", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(1999, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR);


    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
    public int getCode()      { return code; }
    public String getMessage(){ return message; }
    public HttpStatus getStatus() { return status; }
}
