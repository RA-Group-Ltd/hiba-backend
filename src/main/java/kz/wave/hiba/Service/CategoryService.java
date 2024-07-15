package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.CategoryCreateDTO;
import kz.wave.hiba.DTO.CategoryUpdateDTO;
import kz.wave.hiba.Entities.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryCreateDTO categoryCreateDTO);
    Category updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO);
    List<Category> getAllCategories();

    Category getCategoryById(Long id);
}
