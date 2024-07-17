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

/**
 * Implementation of the {@link CategoryService} interface.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Creates a new category.
     *
     * @param categoryCreateDTO the data transfer object containing category creation data
     * @return the created category
     * @throws EntityExistsException if a category with the same name already exists
     */
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

    /**
     * Updates an existing category.
     *
     * @param id the ID of the category to be updated
     * @param categoryUpdateDTO the data transfer object containing category update data
     * @return the updated category
     * @throws EntityNotFoundException if the category is not found
     */
    @Override
    public Category updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(categoryUpdateDTO.getName());
        return categoryRepository.save(category);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to be retrieved
     * @return the category with the specified ID
     * @throws EntityNotFoundException if the category is not found
     */
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

}
