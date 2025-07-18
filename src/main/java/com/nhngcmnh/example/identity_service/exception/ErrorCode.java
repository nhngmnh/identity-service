package com.nhngcmnh.example.identity_service.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "Tên người dùng đã tồn tại"),
    INVALID_USERNAME(1002, "Dữ liệu đầu vào không hợp lệ"),
    INVALID_PASSWORD(1003,"Mật khẩu không đủ 8 ký tự"),
    USER_NOT_EXISTS(1005, "Người dùng không tồn tại"),
    RESOURCE_NOT_FOUND(1004, "Không tìm thấy tài nguyên"),
    INVALID_KEY(9999,"Lỗi không xác định"),
    INTERNAL_ERROR(1999, "Lỗi hệ thống");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode()      { return code; }
    public String getMessage(){ return message; }
}
