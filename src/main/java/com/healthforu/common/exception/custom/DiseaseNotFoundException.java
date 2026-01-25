package com.healthforu.common.exception.custom;

import com.healthforu.common.exception.BusinessException;

public class DiseaseNotFoundException extends BusinessException {
    private static final String DEFAULT_MESSAGE = "해당 질병을 찾을 수 없습니다.";

    public DiseaseNotFoundException() {
        super(DEFAULT_MESSAGE, 404);
    }
}
