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
    public ResponseEntity<?> toggleFavorite(@RequestParam(required = false) ObjectId userId,
                                            @RequestParam ObjectId recipeId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 후 즐겨찾기를 추가해주세요.");
        }

        favoriteService.toggleFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteListResponse>> getFavoriteRecipes(@RequestParam(required = false) ObjectId userId){

        return ResponseEntity.ok(favoriteService.getFavoriteRecipes(userId));
    }





}
