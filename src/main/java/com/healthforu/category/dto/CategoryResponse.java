package com.healthforu.category.dto;

import com.healthforu.category.domain.Category;
import org.bson.types.ObjectId;

public record CategoryResponse(
    ObjectId id,
    Integer sortOrder,
    String iconUrl,
    String categoryName,
    String categorySlug
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getSortOrder(),
            category.getIconUrl(),
            category.getCategoryName(),
            category.getCategorySlug()
        );
    }
}