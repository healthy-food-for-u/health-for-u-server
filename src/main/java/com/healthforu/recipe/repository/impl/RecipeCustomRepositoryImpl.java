package com.healthforu.recipe.repository.impl;

import com.healthforu.disease.domain.Disease;
import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.repository.RecipeCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RecipeCustomRepositoryImpl implements RecipeCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void bulkInsertRecipes(List<Recipe> recipes) {
        if(recipes != null && !recipes.isEmpty()){
            mongoTemplate.insertAll(recipes);
        }
    }

    @Override
    public Page<Recipe> findByExcludeCaution(Disease disease, Pageable pageable) {
        Query query = new Query();

        String cautionRaw = disease.getCaution();

        if (cautionRaw != null && !cautionRaw.isBlank()) {
            List<String> cautionList = Arrays.stream(cautionRaw.split("[,\\s]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            List<Criteria> excludeCriterias = new ArrayList<>();

            for (String caution : cautionList) {
                // recipeName에도 없고 AND ingredients에도 주의 식품이 없는 경우
                excludeCriterias.add(Criteria.where("recipeName").not().regex(".*" + caution + ".*"));
                excludeCriterias.add(Criteria.where("ingredients").not().regex(".*" + caution + ".*"));
            }

            // 모든 제외 조건이 동시에 만족(AND)되어야 함
            if (!excludeCriterias.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(excludeCriterias.toArray(new Criteria[0])));
            }
        }

        long totalCount = mongoTemplate.count(query, Recipe.class);

        query.with(pageable);

        List<Recipe> recipes = mongoTemplate.find(query, Recipe.class);

        return PageableExecutionUtils.getPage(recipes, pageable, () -> totalCount);
    }
}
