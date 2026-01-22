package com.healthforu.category.dto;

import com.healthforu.category.domain.Category;

public record CategoryResponse(
    String id,
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