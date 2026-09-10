package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    void saveCategory(CategoryDTO categoryDTO);
    void updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long id);

    Long getNextCategoryId();
}