package com.healthforu.recipe.dto;

import com.healthforu.recipe.domain.Recipe;
import org.springframework.data.annotation.Id;

import java.util.List;

public record RecipeResponse(
        String id,
        String recipeName,
        String ingredients,
        List<Recipe.ManualStep>manualSteps,
        String recipeThumbnail,
        boolean isCaution,
        boolean isFavorite
) {

    public static RecipeResponse from(Recipe recipe, boolean isCaution, boolean isFavorite){
        return new RecipeResponse(
                recipe.getId(),
                recipe.getRecipeName(),
                recipe.getIngredients(),
                recipe.getManualSteps(),
                recipe.getRecipeThumbnail(),
                isCaution,
                isFavorite
        );
    }

    public static RecipeResponse from(Recipe recipe) {
        return from(recipe, false, false);
    }
}
