package com.healthforu.favorites.service.impl;

import com.healthforu.common.exception.custom.RecipeNotFoundException;
import com.healthforu.common.exception.custom.UserNotFoundException;
import com.healthforu.favorites.domain.FavoriteRecipe;
import com.healthforu.favorites.dto.FavoriteListResponse;
import com.healthforu.favorites.repository.FavoriteRepository;
import com.healthforu.favorites.service.FavoriteService;
import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.repository.RecipeRepository;
import com.healthforu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 즐겨찾기 추가/해제 (Toggle 방식)
     *
     * @param userId
     * @param recipeId
     */
    @Override
    public void toggleFavorite(ObjectId userId, ObjectId recipeId) {

        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException();
        }

        if(!recipeRepository.existsById(recipeId)){
            throw new RecipeNotFoundException();
        }

        favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)
                .ifPresentOrElse(
                        favorite -> favoriteRepository.delete(favorite),
                        () -> {
                            FavoriteRecipe newFavorite = FavoriteRecipe.builder()
                                    .userId(userId)
                                    .recipeId(recipeId)
                                    .build();
                            favoriteRepository.save(newFavorite);
                        }
                );

    }

    /**
     * 특정 유저의 즐겨찾기 목록 가져오기
     *
     * @param userId
     * @return
     */
    @Override
    public List<FavoriteListResponse> getFavoriteRecipes(ObjectId userId) {
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException();
        }

        List<FavoriteRecipe> favorites = favoriteRepository.findByUserId(userId);

        List<ObjectId> recipeIds = favorites.stream()
                .map(FavoriteRecipe::getRecipeId)
                .toList();

        List<Recipe> recipes = recipeRepository.findAllById(recipeIds);

        return recipes.stream()
                .map(recipe -> {
                    ObjectId favoriteId = favorites.stream()
                            .filter(f -> f.getRecipeId().equals(recipe.getId()))
                            .findFirst()
                            .map(FavoriteRecipe::getId)
                            .orElse(new ObjectId());

                    return new FavoriteListResponse(
                            favoriteId.toHexString(),
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getRecipeThumbnail()
                    );
                })
                .toList();
    }

    /**
     * 즐겨찾기 여부 확인 (상세 페이지 로딩 시 필요)
     *
     * @param userId
     * @param recipeId
     * @return
     */
    @Override
    public boolean isFavorite(ObjectId userId, ObjectId recipeId) {
        if (userId == null) {
            return false;
        }

        return favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId);
    }

}
