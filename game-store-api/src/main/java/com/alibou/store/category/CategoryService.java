package com.alibou.store.category;

import com.alibou.store.common.PageResponse;
import com.alibou.store.game.GameRepository;
import com.alibou.store.utils.PaginationUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final GameRepository gameRepository;
    private final CategoryMapper categoryMapper;

    public PageResponse<ShortCategoryResponse> findAllCategories(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        final Page<Category> categoryPaged = categoryRepository.findAll(pageable);

        return PaginationUtils.buildPageResponse(categoryPaged, categoryMapper::toShortCategoryResponse);
    }

    public ShortCategoryResponse findCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .map(categoryMapper::toShortCategoryResponse)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryId + " does not exist"));
    }

    public String saveCategory(final CategoryRequest categoryRequest) {

        if (categoryRepository.existsByNameIgnoreCase(categoryRequest.name())) {
            throw new EntityNotFoundException("Category with name " + categoryRequest.name() + " already exists");
        }

        final Category savedCategory = categoryRepository.save(
                categoryMapper.toCategory(categoryRequest)
        );

        return savedCategory.getId();
    }

    public void updateCategory(String categoryId, final CategoryRequest categoryRequest) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Category with id " + categoryId + " does not exist")
                );

        if (categoryRepository.existsByNameIgnoreCase(categoryRequest.name())) {
            throw new EntityNotFoundException("Category with name " + categoryRequest.name() + " already exists");
        }

        final Category updatedCategory = categoryMapper.toCategory(categoryRequest);
        updatedCategory.setId(category.getId());
        categoryRepository.save(updatedCategory);
    }

    @Transactional
    public void deleteCategory(String categoryId, boolean confirm) {
        long gamesCount = gameRepository.countByCategoryId(categoryId);

        List<String> warnings = new ArrayList<>();

        if(gamesCount > 0) {
            warnings.add("Games count is greater than 0");
            log.warn("Games count is greater than 0");
        }

        if (!warnings.isEmpty() && !confirm) {
            throw new RuntimeException("One or more warnings");
        }

        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Category with id " + categoryId + " does not exist")
                );

        categoryRepository.delete(category);
    }
}
