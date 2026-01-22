package com.healthforu.recipe.service.impl;

import com.healthforu.disease.domain.Disease;
import com.healthforu.disease.repository.DiseaseRepository;
import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.dto.RecipeResponse;
import com.healthforu.recipe.repository.RecipeRepository;
import com.healthforu.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    DiseaseRepository diseaseRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Test
    @DisplayName("특정 질병의 주의 성분을 필터링하고 검색어를 포함한 레시피 목록 조회")
    void getAllRecipes_keyword() {
        Disease disease = Disease.builder().caution("소고기").build();
        ReflectionTestUtils.setField(disease, "id", "disease-1");

        Recipe r1 = Recipe.builder().recipeName("새우 죽").ingredients("새우, 쌀").build();
        Recipe r2 = Recipe.builder().recipeName("소고기 무국").ingredients("소고기, 무").build();

        Page<Recipe> recipePage = new PageImpl<>(List.of(r1, r2));

        when(diseaseRepository.findById("disease-1")).thenReturn(Optional.of(disease));
        when(recipeRepository.findByRecipeNameContaining(eq("국"), any(Pageable.class)))
                .thenReturn(recipePage);

        Page<RecipeResponse> result = recipeService.getAllRecipes("disease-1", Pageable.unpaged(), "국");

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        assertEquals("새우 죽", result.getContent().get(0).recipeName());
        assertFalse(result.getContent().get(0).isCaution(), "새우 죽은 소고기가 없으므로 주의식품이 아님.");

        assertEquals("소고기 무국", result.getContent().get(1).recipeName());
        assertTrue(result.getContent().get(1).isCaution(), "소고기 무국은 소고기가 있으므로  주의식품임.");


        verify(recipeRepository, times(1)).findByRecipeNameContaining(anyString(), any(Pageable.class));
        verify(recipeRepository, never()).findByExcludeCaution(any(), any());

    }

    @Test
    @DisplayName("특정 질병의 주의 성분을 필터링한 레시피 목록 조회")
    void getAllRecipes() {
        Disease disease = Disease.builder().caution("새우").build();
        ReflectionTestUtils.setField(disease, "id", "disease-1");

        Recipe r1 = Recipe.builder().recipeName("새우 죽").ingredients("새우, 쌀").build();
        Recipe r2 = Recipe.builder().recipeName("소고기 무국").ingredients("소고기, 무").build();

        Page<Recipe> filteredPage = new PageImpl<>(List.of(r2));
        when(diseaseRepository.findById("disease-1")).thenReturn(Optional.of(disease));
        when(recipeRepository.findByExcludeCaution(any(Disease.class), any(Pageable.class)))
                .thenReturn(filteredPage);

        Page<RecipeResponse> result = recipeService.getAllRecipes("disease-1", Pageable.unpaged(), null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        assertEquals("소고기 무국", result.getContent().get(0).recipeName());
        assertFalse(result.getContent().get(0).isCaution(), "소고기 무국은 새우가 없으므로 주의식품이 아님.");

        verify(recipeRepository, times(1)).findByExcludeCaution(any(Disease.class), any(Pageable.class));
        verify(recipeRepository, never()).findByRecipeNameContaining(any(), any());

    }

    @Test
    @DisplayName("주의 성분이 들어있는 특정 레시피 조회")
    void getRecipe_caution() {
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "id", "user-1");

        Disease disease = Disease.builder().caution("새우").build();
        ReflectionTestUtils.setField(disease, "id", "disease-1");

        Recipe recipe = Recipe.builder().recipeName("새우 죽").ingredients("새우, 쌀").build();
        ReflectionTestUtils.setField(recipe, "id", "recipe-1");

        when(diseaseRepository.findById(anyString())).thenReturn(Optional.of(disease));
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        RecipeResponse result = recipeService.getRecipe(disease.getId(), recipe.getId(), user.getId());

        assertNotNull(result);
        assertEquals("새우 죽", result.recipeName());
        assertTrue(result.isCaution(),
                "주의 성분인 새우가 포함되어 있으므로 true");

    }

    @Test
    @DisplayName("주의 성분이 들어있는 특정 레시피 조회")
    void getRecipe() {
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "id", "user-1");

        Disease disease = Disease.builder().caution("소고기").build();
        ReflectionTestUtils.setField(disease, "id", "disease-1");

        Recipe recipe = Recipe.builder().recipeName("새우 죽").ingredients("새우, 쌀").build();
        ReflectionTestUtils.setField(recipe, "id", "recipe-1");

        when(diseaseRepository.findById(anyString())).thenReturn(Optional.of(disease));
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        RecipeResponse result = recipeService.getRecipe(disease.getId(), recipe.getId(), user.getId());

        assertNotNull(result);
        assertEquals("새우 죽", result.recipeName());
        assertFalse(result.isCaution(), "주의 성분인 소고기가 포함되어 있지 않으므로 false");

    }
}