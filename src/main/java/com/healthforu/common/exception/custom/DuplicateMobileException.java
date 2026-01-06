package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class DuplicateMobileException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "이미 사용 중인 휴대폰 번호입니다. 다른 휴대폰 번호를 입력해 주세요.";

    public DuplicateMobileException() {
        super(DEFAULT_MESSAGE, 409);
    }
}
