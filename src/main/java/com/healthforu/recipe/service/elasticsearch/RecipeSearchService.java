package com.healthforu.recipe.service.elasticsearch;

import com.healthforu.recipe.dto.RecipeResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RecipeSearchService {

    /**
     * 특정 질환자가 피해야 할 재료(Caution)를 제외하고 검색합니다.
     * (이름에 가중치를 부여하고, Nori 분석기를 통해 검색합니다.)
     */
    Page<RecipeResponse> searchRecipes(String keyword, ObjectId diseaseId, Pageable pageable);

}
