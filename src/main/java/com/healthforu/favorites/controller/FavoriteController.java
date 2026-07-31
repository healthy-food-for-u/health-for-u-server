package com.healthforu.favorites.controller;

import com.healthforu.favorites.dto.FavoriteListResponse;
import com.healthforu.favorites.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleFavorite(@RequestHeader(name = "X-User-Id") String userIdStr,
                                            @RequestParam(value = "recipeId") String recipeIdStr) {

        if (userIdStr == null || userIdStr.isEmpty() || userIdStr.length() != 24) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID 형식입니다: " + userIdStr);
        }

        // 24자리 규격에 맞는지 검증 또는 안전하게 변환
        if (recipeIdStr == null || recipeIdStr.length() != 24) {
            throw new IllegalArgumentException("유효하지 않은 레시피 ID 형식입니다.");
        }

        ObjectId userId = new ObjectId(userIdStr);
        ObjectId recipeId = new ObjectId(recipeIdStr);

        favoriteService.toggleFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteListResponse>> getFavoriteRecipes(@RequestHeader(name = "X-User-Id") String userIdStr){

        ObjectId userId = new ObjectId(userIdStr);

        return ResponseEntity.ok(favoriteService.getFavoriteRecipes(userId));
    }





}
