package com.healthforu.category.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {
    @Id
    private String id;

    private Integer sortOrder;

    private String iconUrl;

    private String categoryName;

    private String categorySlug;

}
