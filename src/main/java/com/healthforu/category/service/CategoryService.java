package com.healthforu.category.service;

import com.healthforu.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    /** 모든 카테고리 조회 (정렬 순서 포함) */
    List<CategoryResponse> getAllCategories();
    
    /** 특정 슬러그로 카테고리 정보 찾기 */
    CategoryResponse getCategoryBySlug(String slug);
}