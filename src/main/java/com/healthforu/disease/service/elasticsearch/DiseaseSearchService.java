package com.healthforu.disease.service.elasticsearch;

import com.healthforu.category.dto.CategoryWithDiseasesResponse;

import java.util.List;

public interface DiseaseSearchService {

    List<CategoryWithDiseasesResponse> searchDiseases(String keyword);
}
