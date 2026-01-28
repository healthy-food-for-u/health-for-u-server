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
                                            @RequestParam(value = "recipeId") ObjectId recipeId) {

        if (userIdStr == null || userIdStr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        ObjectId userId = new ObjectId(userIdStr);

        favoriteService.toggleFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteListResponse>> getFavoriteRecipes(@RequestHeader(name = "X-User-Id") String userIdStr){

        ObjectId userId = new ObjectId(userIdStr);

        return ResponseEntity.ok(favoriteService.getFavoriteRecipes(userId));
    }





}
