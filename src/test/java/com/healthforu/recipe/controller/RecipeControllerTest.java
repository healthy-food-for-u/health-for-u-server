package com.healthforu.recipe.controller;

import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.dto.RecipeResponse;
import com.healthforu.recipe.service.RecipeService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    @DisplayName("레시피 상세 조회")
    void getRecipeDetail() throws Exception {
        Recipe.ManualStep step1 = new Recipe.ManualStep(1, "1번 설명", "image.url");
        Recipe.ManualStep step2 = new Recipe.ManualStep(2, "2번 설명", "image.url");

        RecipeResponse recipeResponse = new RecipeResponse(
                "recipe-1",
                "recipe-1name",
                "새우",
                List.of(step1, step2),
        "recipeThumbnail!",
        false,
                false);

        when(recipeService.getRecipe(any(ObjectId.class), any(ObjectId.class), any(ObjectId.class)))
                .thenReturn(recipeResponse);

        mockMvc.perform(get("/api/recipes/recipe-1")
                .param("diseaseId", "disease-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value("recipe-1name"))
                .andExpect(jsonPath("$.isCaution").value(false))
                .andExpect(jsonPath("$.manualSteps.length()").value(2)) // 단계가 2개인지 확인
                .andExpect(jsonPath("$.manualSteps[0].stepDescription").value("1번 설명")); // 첫 번째 단계 설명 확인

    }

    @Test
    @DisplayName("특정 질병 제외 전체 레시피 조회")
    void getAllRecipes() throws Exception {
        Recipe.ManualStep step1 = new Recipe.ManualStep(1, "1번 설명", "image.url");
        Recipe.ManualStep step2 = new Recipe.ManualStep(2, "2번 설명", "image.url");

        RecipeResponse recipeResponse1 = new RecipeResponse(
                "recipe-1",
                "recipe-1name",
                "새우",
                List.of(step1, step2),
                "recipeThumbnail!",
                false,
                false);

        RecipeResponse recipeResponse2 = new RecipeResponse(
                "recipe-2",
                "recipe-2name",
                "소고기",
                List.of(step1, step2),
                "recipeThumbnail!",
                false,
                false);

        RecipeResponse recipeResponse3 = new RecipeResponse(
                "recipe-1",
                "recipe-1name",
                "마시멜로우",
                List.of(step1, step2),
                "recipeThumbnail!",
                false,
                false);

        Page<RecipeResponse> mockPage = new PageImpl<>(List.of(recipeResponse1, recipeResponse2));

        when(recipeService.getAllRecipes(any(ObjectId.class), any(Pageable.class), isNull()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/recipes")
                        .param("diseaseId", "disease-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recipeName").value("recipe-1name")) // 목록 조회(Page)는 스프링 데이터의 표준 규격에 따라 content라는 배열 안에 데이터가 담김
                .andExpect(jsonPath("$.content[0].isCaution").value(false))
                .andExpect(jsonPath("$.content.length()").value(2)) // 결과가 3개가 아닌 2개인지 확인
                .andExpect(jsonPath("$.content[2]").doesNotExist()); // 3번째 요소는 존재하지 않아야 함

    }
}