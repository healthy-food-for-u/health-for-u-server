package com.healthforu.disease.service;

import com.healthforu.category.dto.CategoryWithDiseasesResponse;
import com.healthforu.disease.dto.DiseaseResponse;

import java.util.List;

public interface DiseaseService {

    /** 개별 질병 상세 정보 */
    DiseaseResponse getDisease(String diseaseId);

    /** 특정 질병 검색 */
    List<CategoryWithDiseasesResponse> searchDiseases(String keyword);

}
