package com.healthforu.recipe.service;

import com.healthforu.recipe.dto.RecipeResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {
    /**
     * 특정 질병의 주의 성분을 필터링하고 검색어(keyword)를 포함한 레시피 목록 조회
     */
    Page<RecipeResponse> getAllRecipes(ObjectId diseaseId, Pageable pageable, String keyword);

    /**
     * 특정 레시피 상세 조회 (사용자의 질병 정보와 대조하여 주의 사항이 있는지 함께 확인)
     */
    RecipeResponse getRecipe(ObjectId diseaseId, ObjectId recipeId, ObjectId userId);
}
