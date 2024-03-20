package kz.wave.hiba.Service.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import kz.wave.hiba.DTO.CategoryCreateDTO;
import kz.wave.hiba.DTO.CategoryUpdateDTO;
import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Repository.CategoryRepository;
import kz.wave.hiba.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryCreateDTO categoryCreateDTO) {
        if (categoryRepository.existsByName(categoryCreateDTO.getName())) {
            throw new EntityExistsException("Category with name " + categoryCreateDTO.getName() + " already exists");
        }

        Category newCategory = new Category();
        newCategory.setName(categoryCreateDTO.getName());

        if (categoryCreateDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryCreateDTO.getParentCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
            newCategory.setParentCategoryId(categoryCreateDTO.getParentCategoryId());
        }

        return categoryRepository.save(newCategory);
    }

    @Override
    public Category updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(categoryUpdateDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

}
