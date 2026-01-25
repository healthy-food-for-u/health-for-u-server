package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class RecipeNotFoundException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "해당 레시피를 찾을 수 없습니다.";

    public RecipeNotFoundException() {
        super(DEFAULT_MESSAGE, 404);
    }
}
