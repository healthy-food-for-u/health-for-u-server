package com.healthforu.favorites.repository;

import com.healthforu.favorites.domain.FavoriteRecipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<FavoriteRecipe, ObjectId> {

    Optional<FavoriteRecipe> findByUserIdAndRecipeId(ObjectId userId, ObjectId recipeId);

    List<FavoriteRecipe> findByUserId(ObjectId userId);

    boolean existsByUserIdAndRecipeId(ObjectId userId, ObjectId recipeId);
}
