package com.alibou.store.category;


import com.alibou.store.category.dto.CategoryRequest;
import com.alibou.store.category.dto.CategoryResponse;
import com.alibou.store.category.dto.CategoryResponseFull;
import com.alibou.store.common.PageResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public PageResponse<CategoryResponse> findAll(final int pageNumber, final int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "name"));

        Page<Category> categories = categoryRepository.findAll(pageRequest);
        List<CategoryResponse> categoryResponseList = categories
                .map(categoryMapper::toCategoryResponse)
                .toList();

        return new PageResponse<>(
                categoryResponseList,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isFirst(),
                categories.isLast()
        );
    }

    public PageResponse<CategoryResponseFull> findAllWithGames(int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "name"));

        Page<Category> categories = categoryRepository.findAllWithGames(pageRequest);
        List<CategoryResponseFull> categoryResponseList = categories
                .map(categoryMapper::toCategoryResponseFull)
                .toList();

        return new PageResponse<>(
            categoryResponseList,
            categories.getNumber(),
            categories.getSize(),
            categories.getTotalElements(),
            categories.getTotalPages(),
            categories.isFirst(),
            categories.isLast()
        );
    }

    public CategoryResponseFull findById(final String id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow( () -> {
                    log.warn("Category with id {} not found", id);
                    return new EntityNotFoundException("The category with id " + id + " does not exist.");
                });

        return categoryMapper.toCategoryResponseFull(category);
    }

    public CategoryResponse findByName(
            @NotBlank(message = "The name cannot be null or empty") String catName) {

        Category category = categoryRepository.findByName(catName);
        if (Objects.isNull(category)) {
            throw new EntityNotFoundException("The category with name " + catName + " does not exist");
        }
        return categoryMapper.toCategoryResponse(category);
    }

    public String create(final CategoryRequest categoryRequest) {

        if (categoryRepository.existByName(categoryRequest.getName())) {
            log.warn("The category with name {} already exists.", categoryRequest.getName());
            throw new EntityExistsException("The category with name " + categoryRequest.getName() + " already exists.");
        }

        Category categoryToPersist = categoryMapper.toCategory(categoryRequest);

        return categoryRepository.save(categoryToPersist).getId();
    }

    public String update(final String id, final CategoryRequest categoryRequest) {

        Category categoryToUpdated = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The category with id " + id + " does not exist."));

        if (!categoryRequest.getName().equals(categoryToUpdated.getName())
            && categoryRepository.existByName(categoryRequest.getName())
        ) {
            log.warn("The category with name {} already exists.", categoryRequest.getName());
            throw new EntityExistsException("The category with name " + categoryRequest.getName() + " already exists.");
        }

        categoryToUpdated.setName(categoryRequest.getName());
        categoryToUpdated.setDescription(categoryRequest.getDescription());

        return categoryRepository.save(categoryToUpdated).getId();
    }

    public void deleteById(final String id) {
        categoryRepository.deleteById(id);
    }
}
