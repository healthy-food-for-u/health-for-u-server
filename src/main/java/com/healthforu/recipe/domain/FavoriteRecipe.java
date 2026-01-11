package com.healthforu.recipe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "favorite_recipes")
@Getter
@NoArgsConstructor
public class FavoriteRecipe {

    @Id
    private String id;

    @DBRef(db="recipes")
    private String recipeId;

    @DBRef(db="users")
    private String userId;

}
