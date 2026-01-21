package com.healthforu.favorites.repository;

import com.healthforu.favorites.domain.FavoriteRecipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<FavoriteRecipe, String> {

    Optional<FavoriteRecipe> findByUserIdAndRecipeId(String userId, String recipeId);

    List<FavoriteRecipe> findByUserId(String userId);

    boolean existsByUserIdAndRecipeId(String userId, String recipeId);
}
