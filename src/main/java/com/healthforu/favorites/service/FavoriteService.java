package com.healthforu.favorites.service;

import com.healthforu.favorites.dto.FavoriteListResponse;
import org.bson.types.ObjectId;

import java.util.List;

public interface FavoriteService {
    // 즐겨찾기 추가/해제 (Toggle 방식)
    void toggleFavorite(ObjectId userId, ObjectId recipeId);
    
    // 특정 유저의 즐겨찾기 목록 가져오기
    List<FavoriteListResponse> getFavoriteRecipes(ObjectId userId);
    
    // 즐겨찾기 여부 확인 (상세 페이지 로딩 시 필요)
    boolean isFavorite(ObjectId userId, ObjectId recipeId);
}
