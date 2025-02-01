package com.alibou.store.category;

import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .description(categoryRequest.description())
                .build();
    }

    public ShortCategoryResponse toShortCategoryResponse(Category category) {
        return ShortCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
