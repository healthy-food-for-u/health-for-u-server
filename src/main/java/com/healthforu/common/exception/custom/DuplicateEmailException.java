package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class DuplicateEmailException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "이미 사용 중인 이메일입니다. 다른 이메일을 입력해 주세요.";

    public DuplicateEmailException() {
        super(DEFAULT_MESSAGE, 409);
    }
}
