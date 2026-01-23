package com.healthforu.category.dto;

import com.healthforu.disease.dto.DiseaseResponse;
import org.bson.types.ObjectId;

import java.util.List;

// 검색 결과 및 초기 화면을 위한 통합 DTO
public record CategoryWithDiseasesResponse(
        String categoryId,
        String categoryName,
        String iconUrl,
        List<DiseaseResponse> diseases // 해당 카테고리에 속한 질병들
) {}