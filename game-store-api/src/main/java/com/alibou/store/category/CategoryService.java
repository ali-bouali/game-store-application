package com.alibou.store.category;

import com.alibou.store.category.dto.CategoryRequest;
import com.alibou.store.category.dto.CategoryResponse;
import com.alibou.store.common.PageResponse;
import com.alibou.store.game.GameRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final GameRepository gameRepository;
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


    public CategoryResponse findById(final String id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow( () -> {
                    return new EntityNotFoundException("The category with id " + id + " does not exist.");
                });

        return categoryMapper.toCategoryResponse(category);
    }


    public String saveCategory(final CategoryRequest categoryRequest) {

        if (categoryRepository.existByName(categoryRequest.getName())) {
            throw new EntityExistsException("The category with name " + categoryRequest.getName() + " already exists.");
        }

        Category categoryToPersist = categoryMapper.toCategory(categoryRequest);

        return categoryRepository.save(categoryToPersist).getId();
    }

    public String updateCategory(final String id, final CategoryRequest categoryRequest) {

        Category categoryToUpdated = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The category with id " + id + " does not exist."));

        if (!categoryRequest.getName().equals(categoryToUpdated.getName())
            && categoryRepository.existByName(categoryRequest.getName())
        ) {
            throw new EntityExistsException("The category with name " + categoryRequest.getName() + " already exists.");
        }

        categoryToUpdated.setName(categoryRequest.getName());
        categoryToUpdated.setDescription(categoryRequest.getDescription());

        return categoryRepository.save(categoryToUpdated).getId();
    }

    public void deleteById(final String categoryId, boolean isConfirm) {

        long gameCount = 0;

        if (!isConfirm) {
            gameCount = gameRepository.countByCategoryId(categoryId);
         }

        if (gameCount > 0) {
            throw new CategoryException("Category has " + gameCount + " game");
        }

        categoryRepository.deleteById(categoryId);

    }
}
