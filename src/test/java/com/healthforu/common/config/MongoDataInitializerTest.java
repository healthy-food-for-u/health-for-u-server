package com.healthforu.common.config;

import com.healthforu.config.MongoDataInitializer;
import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(MongoTestConfig.class)
class MongoDataInitializerTest {

    @Autowired
    private MongoDataInitializer mongoDataInitializer;

    @Autowired
    private RecipeRepository recipeRepository;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    @DisplayName("외부 API로부터 데이터를 받아와서 MongoDB에 벌크 삽입")
    void importRecipesFromExternalApi() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> cookRcp01 = new HashMap<>();

        Map<String, Object> row = new HashMap<>();
        row.put("RCP_NM", "가짜 새우 요리");
        row.put("RCP_PARTS_DTLS", "새우, 소금");
        row.put("ATT_FILE_NO_MK", "http://image.com/thumb.jpg");
        row.put("MANUAL01", "1. 새우를 씻는다.");

        cookRcp01.put("row", List.of(row));
        mockResponse.put("COOKRCP01", cookRcp01);

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(mockResponse);

        mongoDataInitializer.importRecipesFromExternalApi();

        List<Recipe> savedRecipes = recipeRepository.findAll();

        assertThat(savedRecipes).hasSize(1);
        assertThat(savedRecipes.get(0).getRecipeName()).isEqualTo("가짜 새우 요리");
        assertThat(savedRecipes.get(0).getManualSteps()).isNotEmpty();

        Recipe savedRecipe = savedRecipes.get(0);
        assertThat(savedRecipe.getManualSteps().get(0).getStepDescription())
                .contains("새우를 씻는다");
    }
}