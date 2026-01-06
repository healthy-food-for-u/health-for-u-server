package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class DuplicateUserException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "이미 사용 중인 아이디입니다. 다른 아이디를 입력해 주세요.";

    public DuplicateUserException() {
        super(DEFAULT_MESSAGE, 409);
    }
}
