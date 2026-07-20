package com.sprintlog.sprintlogboot.exception;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // 기본 메세지(ErrorCord의 DefaultMessage를)로 던진다.
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    // 상세 메세지를 직접 주어 던진다.
    public BusinessException(ErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
    }

    // 예외 변환용 - 저수준 예외를 감싸 비지니스 예외로 변환해서 던진다.
    public BusinessException(ErrorCode errorCode, String detail, Throwable cause) {
        super(detail, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }



}
