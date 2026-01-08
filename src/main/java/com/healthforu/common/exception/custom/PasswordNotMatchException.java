package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class PasswordNotMatchException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordNotMatchException() {
        super(DEFAULT_MESSAGE, 400);
    }
}
