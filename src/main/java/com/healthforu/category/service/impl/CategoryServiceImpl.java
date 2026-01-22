package com.healthforu.category.service.impl;

import com.healthforu.category.domain.Category;
import com.healthforu.category.dto.CategoryResponse;
import com.healthforu.category.repository.CategoryRepository;
import com.healthforu.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 모든 카테고리 조회 (정렬 순서 포함)
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getSortOrder(),
                        category.getIconUrl(),
                        category.getCategoryName(),
                        category.getCategorySlug()
                ))
                .toList();
    }

    /**
     * 특정 슬러그로 카테고리 정보 찾기
     *
     * @param slug
     */
    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        return null;
    }
}
