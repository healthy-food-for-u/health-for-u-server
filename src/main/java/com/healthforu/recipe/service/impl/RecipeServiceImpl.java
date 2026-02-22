package com.healthforu.recipe.service.impl;

import com.healthforu.common.exception.custom.DiseaseNotFoundException;
import com.healthforu.common.exception.custom.RecipeNotFoundException;
import com.healthforu.disease.domain.Disease;
import com.healthforu.disease.repository.DiseaseRepository;
import com.healthforu.favorites.repository.FavoriteRepository;
import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.dto.RecipeResponse;
import com.healthforu.recipe.repository.RecipeRepository;
import com.healthforu.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final DiseaseRepository diseaseRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 특정 질병의 주의 성분을 필터링하고 검색어(keyword)를 포함한 레시피 목록 조회
     *
     * @param diseaseId
     * @param pageable
     * @param keyword
     */
    @Override
    public Page<RecipeResponse> getAllRecipes(ObjectId diseaseId, Pageable pageable, String keyword) {
        Disease disease = diseaseRepository.findById(diseaseId)
                .orElseThrow(() -> new DiseaseNotFoundException());

        Page<Recipe> recipePage;
        if(keyword != null && !keyword.isBlank()){
            recipePage = recipeRepository.findByRecipeNameContaining(keyword, pageable);

            return recipePage.map(recipe -> {
                boolean isCaution = checkCaution(recipe, disease);
                return RecipeResponse.from(recipe, isCaution, false);
            });

        } else {
            return recipeRepository.findByExcludeCaution(disease, pageable)
                    .map(recipe -> RecipeResponse.from(recipe, false, false));
        }

    }

    /**
     * 특정 레시피 상세 조회 (사용자의 질병 정보와 대조하여 주의 사항이 있는지 함께 확인)
     *
     * @param diseaseId
     * @param recipeId
     */
    @Override
    public RecipeResponse getRecipe(ObjectId diseaseId, ObjectId recipeId, ObjectId userId) {
        log.info("조회 요청 - 질환ID: {}, 레시피ID: {}", diseaseId, recipeId);

        Disease disease = diseaseRepository.findById(diseaseId)
                .orElseThrow(() -> new DiseaseNotFoundException());

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException());

        boolean isCaution = checkCaution(recipe, disease);
        boolean isFavorite = false;

        if (userId != null) {
            isFavorite = favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId);
        }

        return RecipeResponse.from(recipe, isCaution, isFavorite);
    }

    private boolean checkCaution(Recipe recipe, Disease disease){
        if(disease.getCaution() == null || disease.getCaution().isBlank() || recipe.getIngredients() == null){
            return false;
        }

        return recipe.getIngredients().contains(disease.getCaution());
    }
}
