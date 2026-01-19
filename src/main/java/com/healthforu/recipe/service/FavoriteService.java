package com.healthforu.recipe.service;

import com.healthforu.recipe.dto.RecipeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    // 즐겨찾기 추가/해제 (Toggle 방식)
    void toggleFavorite(String userId, String recipeId);
    
    // 특정 유저의 즐겨찾기 목록 가져오기
    Page<RecipeResponse> getFavoriteRecipes(String userId, Pageable pageable);
    
    // 즐겨찾기 여부 확인 (상세 페이지 로딩 시 필요)
    boolean isFavorite(String userId, String recipeId);
}