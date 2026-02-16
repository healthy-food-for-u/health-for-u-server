package com.healthforu.config;

import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.domain.RecipeDocument;
import com.healthforu.recipe.repository.RecipeRepository;
import com.healthforu.recipe.repository.elasticsearch.RecipeSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecipeDataInitializer implements CommandLineRunner {

    private final RecipeRepository mongoRecipeRepository;
    private final RecipeSearchRepository recipeSearchRepository;

    @Override
    public void run(String... args) throws Exception {
        if(recipeSearchRepository.count() > 0){
            log.info("Elasticsearch에 이미 데이터가 존재합니다. 마이그레이션을 건너뜁니다.");
            return;
        }

        log.info("MongoDB에서 Elasticsearch로 데이터 마이그레이션을 시작합니다.");

        List<Recipe> allRecipes = mongoRecipeRepository.findAll();

        List<RecipeDocument> documents = allRecipes.stream()
                .map(RecipeDocument::from)
                .collect(Collectors.toList());

        recipeSearchRepository.saveAll(documents);
    }
}
