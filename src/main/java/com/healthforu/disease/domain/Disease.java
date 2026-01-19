package com.healthforu.disease.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "diseases")
@Getter
@NoArgsConstructor
public class Disease {

    @Id
    private String id;

    @Indexed(unique = true)
    private String diseaseName;

    private String caution;

    private String etc;

    private String foodCategory;

    private String recommended;

    @DBRef(db="categories")
    private String categoryId;

}
