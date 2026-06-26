package com.healthforu.disease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthforu.disease.domain.Disease;
import org.bson.types.ObjectId;

@JsonInclude(JsonInclude.Include.NON_NULL) // 값이 null인 필드는 JSON 결과에서 아예 제외
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
