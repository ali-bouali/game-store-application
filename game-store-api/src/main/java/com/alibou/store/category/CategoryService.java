package com.alibou.store.category;

import com.alibou.store.common.PageResponse;
import com.alibou.store.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public PageResponse<CategoryResponseShort> findAllCategories(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        final Page<Category> categoryPaged = categoryRepository.findAll(pageable);

        return PaginationUtils.buildPageResponse(categoryPaged, categoryMapper::toCategoryResponseShort);
    }

    public CategoryResponseShort findCategoryById(String categoryId) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category with id {} does not exist", categoryId);
                    return new RuntimeException("Category with id " + categoryId + " does not exist");
                });

        return categoryMapper.toCategoryResponseShort(category);
    }

    public PageResponse<CategoryResponseFull> findAllCategoriesWithGamesAndPlatforms(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        final Page<Category> categoryPaged = categoryRepository.findAllWithGamesAndPlatforms(pageable);

        return PaginationUtils.buildPageResponse(categoryPaged, categoryMapper::toCategoryResponseFull);
    }

    public CategoryResponseFull findCategoryByIdWithGamesAndPlatforms(String categoryId) {
        final Category category = categoryRepository.findByIdWithGamesAndPlatforms(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category with id {} does not exist", categoryId);
                    return new RuntimeException("Category with id " + categoryId + " does not exist");
                });

        return categoryMapper.toCategoryResponseFull(category);
    }

    public String saveCategory(final CategoryRequest categoryRequest) {

        if (categoryRepository.existsByName(categoryRequest.name())) {
            log.warn("Category with name {} already exists", categoryRequest.name());
            throw new RuntimeException("Category with name " + categoryRequest.name() + " already exists");
        }

        final Category savedCategory = categoryRepository.save(
                categoryMapper.toCategory(categoryRequest)
        );

        return savedCategory.getId();
    }

    public void updateCategory(String categoryId, final CategoryRequest categoryRequest) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category with id {} does not exist", categoryId);
                    return new RuntimeException("Category with id " + categoryId + " does not exist");
                });

        final Category updatedCategory = categoryMapper.toCategory(categoryRequest);
        updatedCategory.setId(category.getId());
        categoryRepository.save(updatedCategory);
    }

    public void deleteCategory(String categoryId) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category with id {} does not exist", categoryId);
                    return new RuntimeException("Category with id " + categoryId + " does not exist");
                });

        categoryRepository.delete(category);
    }
}
