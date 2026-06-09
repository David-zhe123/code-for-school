package com.travel.common.error;

public final class ErrorCode {
    private ErrorCode() {
    }

    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_ERROR = 500;

    public static final int BUSINESS_ERROR = 10000;
    public static final int USERNAME_EXISTS = 10001;
    public static final int LOGIN_FAILED = 10002;
    public static final int USER_DISABLED = 10003;
}

