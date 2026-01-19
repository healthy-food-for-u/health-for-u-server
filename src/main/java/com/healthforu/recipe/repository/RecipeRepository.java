package com.healthforu.recipe.repository;

import com.healthforu.recipe.domain.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RecipeRepository extends MongoRepository<Recipe, String>, RecipeCustomRepository {

}
