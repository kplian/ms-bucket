package com.kplian.bucket.domain.exception;

public class SystemException extends RuntimeException {
    private final String code;

    public SystemException(String message, String code) {
        super(message);
        this.code = code;
    }

    public SystemException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
