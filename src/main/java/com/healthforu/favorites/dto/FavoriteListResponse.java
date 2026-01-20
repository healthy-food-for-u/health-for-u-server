package com.healthforu.favorites.dto;

public record FavoriteListResponse(
    String favoriteId,
    String recipeId,
    String recipeName,
    String recipeThumbnail
) {}