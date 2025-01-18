package com.alibou.store.category;

import com.alibou.store.category.dto.CategoryRequest;
import com.alibou.store.category.dto.CategoryResponse;
import com.alibou.store.category.dto.CategoryResponseFull;
import com.alibou.store.game.GameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryMapper {

    private final GameMapper gameMapper;

    public CategoryResponse toCategoryResponse(Category category) {

        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public Category toCategory(CategoryRequest categoryRequest) {

       return Category
               .builder()
               .name(categoryRequest.getName())
               .description(categoryRequest.getDescription())
               .build();
    }

    public CategoryResponseFull toCategoryResponseFull(Category category) {

        return CategoryResponseFull
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .games(gameMapper.toGameResponses(category.getGames()))
                .build();
    }


}
