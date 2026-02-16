package com.healthforu.recipe.repository.elasticsearch;

import com.healthforu.recipe.domain.RecipeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeDocument, String> {
}
