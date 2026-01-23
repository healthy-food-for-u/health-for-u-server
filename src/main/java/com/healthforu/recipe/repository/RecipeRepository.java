package com.healthforu.recipe.repository;

import com.healthforu.recipe.domain.Recipe;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RecipeRepository extends MongoRepository<Recipe, ObjectId>, RecipeCustomRepository {
    // 주의 성분과 상관없이 키워드가 포함된 모든 레시피 검색
    Page<Recipe> findByRecipeNameContaining(String keyword, Pageable pageable);

}
