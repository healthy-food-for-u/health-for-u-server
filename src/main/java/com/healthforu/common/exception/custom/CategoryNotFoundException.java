package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class CategoryNotFoundException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "해당 카테고리를 찾을 수 없습니다.";
    public CategoryNotFoundException() {
        super(DEFAULT_MESSAGE, 404);
    }
}
