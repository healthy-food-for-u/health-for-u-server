package com.healthforu.recipe.controller;

import com.healthforu.recipe.dto.RecipeResponse;
import com.healthforu.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeDetail(
            @PathVariable("recipeId") String recipeId,
            @RequestParam("diseaseId") String diseaseId) {

        return ResponseEntity.ok(recipeService.getRecipe(diseaseId, recipeId));
    }

    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> getAllRecipes(
            @RequestParam("diseaseId") String diseaseId,
            @RequestParam(value = "keyword", required = false) String keyword,
            Pageable pageable) {

        return ResponseEntity.ok(recipeService.getAllRecipes(diseaseId, pageable, keyword));
    }
}
