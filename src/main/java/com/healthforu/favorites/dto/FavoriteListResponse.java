package com.healthforu.favorites.dto;

import org.bson.types.ObjectId;

public record FavoriteListResponse(
    String id,
    ObjectId recipeId,
    String recipeName,
    String recipeThumbnail
) {}