package com.healthforu.recipe.controller;

import com.healthforu.config.RecipeDataInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/recipes")
@RequiredArgsConstructor
public class RecipeAdminController {

    private final RecipeDataInitializer recipeDataInitializer;

    @PostMapping("/import")
    public String initData() {
        recipeDataInitializer.importRecipesFromExternalApi();

        return "Recipe data import started!";
    }
}