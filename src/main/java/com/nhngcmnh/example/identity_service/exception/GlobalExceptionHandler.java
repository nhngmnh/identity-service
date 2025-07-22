

package com.nhngcmnh.example.identity_service.exception;
import org.springframework.security.access.AccessDeniedException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nhngcmnh.example.identity_service.dto.request.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
        /* ---------- LỖI KHÔNG ĐỦ QUYỀN ---------- */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(
                ErrorCode.UNAUTHORIZATION,
                ErrorCode.UNAUTHORIZATION.getStatus(),
                null
        );
    }

    /* ---------- LỖI ĐÃ ĐỊNH NGHĨA BẰNG AppException ---------- */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        return buildResponse(
                ex.getErrorCode(),           // mã lỗi đặc thù
                ex.getErrorCode().getStatus(),
                null
        );
    }
    /* ---------- NOT FOUND ---------- */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoSuchElement(NoSuchElementException ex) {
        return buildResponse(
                ErrorCode.RESOURCE_NOT_FOUND,
                ErrorCode.RESOURCE_NOT_FOUND.getStatus(),
                null
        );
    }

    /* ---------- Fallback cho mọi RuntimeException chưa bắt ---------- */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntime(RuntimeException ex) {
        return buildResponse(
                ErrorCode.INTERNAL_ERROR,
                ErrorCode.INTERNAL_ERROR.getStatus(),
                null
        );
    }

    /* ---------- Fallback cuối cùng ---------- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        return buildResponse(
                ErrorCode.INTERNAL_ERROR,
                ErrorCode.INTERNAL_ERROR.getStatus(),
                null
        );
    }

    /* ---------- Helper chung ---------- */
    private ResponseEntity<ApiResponse<Object>> buildResponse(
            ErrorCode errorCode,
            HttpStatus status,
            Object data) {

        ApiResponse<Object> body = new ApiResponse<>(
                false,
                errorCode.getMessage(),
                errorCode.getCode(),
                data
        );
        return ResponseEntity.status(status).body(body);
    }
    /* ---------- LỖI VALIDATION ---------- */
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        ErrorCode errorCode = ErrorCode.INVALID_KEY; // fallback mặc định
        if (fieldError != null) {
            try {
                // Giả sử message chính là tên của ErrorCode enum
                errorCode = ErrorCode.valueOf(fieldError.getDefaultMessage());
            } catch (IllegalArgumentException e) {
                // Nếu không khớp enum nào, giữ fallback là INVALID_KEY
            }
        }
        ApiResponse<Object> body = new ApiResponse<>(
                false,
                errorCode.getMessage(),
                errorCode.getCode(),
                null
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }


}
