package com.healthforu.disease.dto;

import com.healthforu.disease.domain.Disease;
import org.bson.types.ObjectId;

public record DiseaseResponse(
    String id,
    String diseaseName,
    String caution,
    String foodCategory,
    ObjectId categoryId
) {
    public static DiseaseResponse from(Disease disease){
        return new DiseaseResponse(
                disease.getId().toHexString(),
                disease.getDiseaseName(),
                disease.getCaution(),
                disease.getFoodCategory(),
                disease.getCategoryId()
        );
    }
}
