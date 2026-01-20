package com.healthforu.favorites.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "favorite_recipes")
@Getter
@NoArgsConstructor
public class FavoriteRecipe {

    @Id
    private String id;

    private String recipeId;

    private String userId;

}
