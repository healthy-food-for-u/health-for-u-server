package com.healthforu.category.service;

import com.healthforu.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    /** 모든 카테고리 조회 (오름차순 정렬) */
    List<CategoryResponse> getAllCategories();
}