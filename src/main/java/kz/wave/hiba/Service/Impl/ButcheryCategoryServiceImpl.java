package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.ButcheryCategoryCreateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.ButcheryCategory;
import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Repository.ButcheryCategoryRepository;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.CategoryRepository;
import kz.wave.hiba.Service.ButcheryCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ButcheryCategoryService} interface.
 */
@Service
public class ButcheryCategoryServiceImpl implements ButcheryCategoryService {

    private final ButcheryCategoryRepository butcheryCategoryRepository;

    @Autowired
    public ButcheryCategoryServiceImpl(ButcheryCategoryRepository butcheryCategoryRepository) {
        this.butcheryCategoryRepository = butcheryCategoryRepository;
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ButcheryRepository butcheryRepository;

    /**
     * Create new Butchery Category
     *
     * @param butcheryCategoryCreateDTO the butchery category creation data transfer object
     * @return the created new butchery category
     */
    @Override
    public void createButcheryCategory(ButcheryCategoryCreateDTO butcheryCategoryCreateDTO) {
        ButcheryCategory butcheryCategory = new ButcheryCategory();
        Optional<Category> categoryOptional = categoryRepository.findById(butcheryCategoryCreateDTO.getCategoryId());

        if (categoryOptional.isEmpty()) {
            return;
        }

        Category category = categoryOptional.get();

        if (category.getParentCategoryId() != null) {
            return;
        }

        butcheryCategory.setCategoryId(category);

        Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryCategoryCreateDTO.getButcheryId());

        if (butcheryOptional.isEmpty()) {
            return;
        }

        Butchery butchery = butcheryOptional.get();
        butcheryCategory.setButcheryId(butchery.getId());

        butcheryCategoryRepository.save(butcheryCategory);
    }

    /**
     * Get categories by butchery id
     *
     * @param butcheryId gets category by butchery id
     * @return butchery category
     */
    @Override
    public List<ButcheryCategory> getCategoriesByButcheryId(Long butcheryId) {
        List<ButcheryCategory> bcs = butcheryCategoryRepository.getButcheryCategoriesByButcheryId(butcheryId);
        return bcs;
    }
}
