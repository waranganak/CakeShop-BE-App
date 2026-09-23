package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.CategoryDTO;
import lk.ijse.DreamsCake.entity.Category;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.exception.ResourceNotFoundException;
import lk.ijse.DreamsCake.repository.CategoryRepo;
import lk.ijse.DreamsCake.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo ) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories from database");
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        for (Category category : categories) {
            categoryDTOs.add(new CategoryDTO(
                    category.getId(),
                    category.getCategoryName(),
                    category.getDescription()
            ));
        }
        return categoryDTOs;
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        log.info("Saving category: {}", categoryDTO.getCategoryName());

        if (categoryDTO.getCategoryName() == null || categoryDTO.getCategoryName().trim().isEmpty()) {
            log.warn("Category name cannot be empty");
            throw new ApiException(400, "Category name cannot be empty!");
        }

        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());
        categoryRepo.save(category);

    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        log.info("Updating category ID: {}", categoryDTO.getId());

        Category category = categoryRepo.findById(categoryDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryDTO.getId()));

        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());
        categoryRepo.save(category);

    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category ID: {}", id);

        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        categoryRepo.deleteById(id);
    }

    @Override
    public Long getNextCategoryId() {
        Long maxId = categoryRepo.findMaxId();
        return (maxId == null) ? 1L : maxId + 1;
    }
}