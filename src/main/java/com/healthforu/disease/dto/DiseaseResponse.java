package com.healthforu.disease.dto;

import com.healthforu.disease.domain.Disease;

public record DiseaseResponse(
    String id,
    String diseaseName,
    String caution,
    String foodCategory,
    String categoryId
) {
    public static DiseaseResponse from(Disease disease){
        return new DiseaseResponse(
                disease.getId(),
                disease.getDiseaseName(),
                disease.getCaution(),
                disease.getFoodCategory(),
                disease.getCategoryId()
        );
    }
}
