package com.healthforu.recipe.repository;

import com.healthforu.common.config.MongoTestConfig;
import com.healthforu.disease.domain.Disease;
import com.healthforu.recipe.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(MongoTestConfig.class)
class RecipeRepositoryTest {

    // 내장 DB와 연결된 템플릿 주입
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    // Recipe 컬렉션의 모든 데이터를 지우고 시작
    @BeforeEach
    void cleanUp() {
        mongoTemplate.dropCollection(Recipe.class);
    }

    @Test
    @DisplayName("레시피 데이터가 잘 들어가는지 확인")
    void bulkInsertRecipes(){
        Recipe r1 = Recipe.builder().recipeName("새우 죽").ingredients("새우, 쌀").build();
        Recipe r2 = Recipe.builder().recipeName("소고기 무국").ingredients("소고기, 무").build();
        List<Recipe> recipeList = List.of(r1, r2);

        recipeRepository.bulkInsertRecipes(recipeList);

        List<Recipe> savedRecipes = recipeRepository.findAll();

        assertThat(savedRecipes).hasSize(2);

        assertThat(savedRecipes)
                .extracting(Recipe::getRecipeName)
                .containsExactlyInAnyOrder("새우 죽", "소고기 무국");
    }

    @Test
    @DisplayName("주의 성분이 포함된 레시피가 필터링되는지 확인")
    void findByExcludeCautionTest() {
        Recipe r1 = Recipe.builder().recipeName("새우 죽").ingredients("새우, 쌀").build();
        Recipe r2 = Recipe.builder().recipeName("소고기 무국").ingredients("소고기, 무").build();
        recipeRepository.saveAll(List.of(r1, r2));

        Disease disease = new Disease();
        ReflectionTestUtils.setField(disease, "caution", "새우"); // "새우"가 들어간건 걸러져야 함

        Page<Recipe> result = recipeRepository.findByExcludeCaution(disease, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getRecipeName()).isEqualTo("소고기 무국");
    }
}