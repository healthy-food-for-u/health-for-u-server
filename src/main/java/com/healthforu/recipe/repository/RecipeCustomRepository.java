package com.healthforu.recipe.repository;

import com.healthforu.disease.domain.Disease;
import com.healthforu.recipe.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeCustomRepository {
    // 벌크 업로드용
    void bulkInsertRecipes(List<Recipe> recipes);

    // 질병 주의사항 제외 검색 (동적 쿼리)
    Page<Recipe> findByExcludeCaution(Disease disease, Pageable pageable);
}
