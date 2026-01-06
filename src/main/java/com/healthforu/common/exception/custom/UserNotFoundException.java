package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE, 404);
    }
}
