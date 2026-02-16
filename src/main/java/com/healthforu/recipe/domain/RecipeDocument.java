package com.healthforu.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "recipes")
@Setting(settingPath = "elasticsearch/settings.json")
public class RecipeDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String recipeName;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String ingredients;

    private String recipeThumbnail;

    public static RecipeDocument from(Recipe recipe) {
        return RecipeDocument.builder()
                .id(recipe.getId().toHexString())
                .recipeName(recipe.getRecipeName())
                .ingredients(recipe.getIngredients())
                .recipeThumbnail(recipe.getRecipeThumbnail())
                .build();
    }

}
