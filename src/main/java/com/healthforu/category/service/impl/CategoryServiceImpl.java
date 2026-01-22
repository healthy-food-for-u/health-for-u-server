package com.healthforu.category.service.impl;

import com.healthforu.category.domain.Category;
import com.healthforu.category.dto.CategoryResponse;
import com.healthforu.category.repository.CategoryRepository;
import com.healthforu.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 모든 카테고리 조회 (오름차순 정렬)
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        Sort sort = Sort.by(Sort.Direction.ASC, "sortOrder");

        List<Category> categories = categoryRepository.findAll(sort);

        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }

}
