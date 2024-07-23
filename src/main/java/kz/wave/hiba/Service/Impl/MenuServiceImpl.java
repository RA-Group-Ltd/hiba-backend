package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.MenuCreateDTO;
import kz.wave.hiba.DTO.MenuUpdateDTO;
import kz.wave.hiba.Entities.ButcheryCategory;
import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Repository.ButcheryCategoryRepository;
import kz.wave.hiba.Repository.CategoryRepository;
import kz.wave.hiba.Repository.MenuRepository;
import kz.wave.hiba.Service.MenuFileUploadService;
import kz.wave.hiba.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link MenuService} interface.
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ButcheryCategoryRepository butcheryCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuFileUploadService menuFileUploadService;

    /**
     * Retrieves all menus.
     *
     * @return a list of all menus
     */
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /**
     * Retrieves a menu by its ID.
     *
     * @param id the ID of the menu
     * @return the menu found by ID
     */
    @Override
    public Menu getOneMenu(Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    /**
     * Creates a new menu.
     *
     * @param menuCreateDTO the data transfer object containing menu creation data
     * @return the created menu, or null if the butchery category or category is not found
     */
    @Override
    public Menu createMenu(MenuCreateDTO menuCreateDTO) {

        Menu newMenu = new Menu();
        newMenu.setName(menuCreateDTO.getName());
        newMenu.setIsWholeAnimal(menuCreateDTO.getIsWholeAnimal());
        newMenu.setWeight(menuCreateDTO.getWeight());
        newMenu.setPrice(menuCreateDTO.getPrice());
        newMenu.setDescription(menuCreateDTO.getDescription());

        Optional<ButcheryCategory> butcheryOptional = butcheryCategoryRepository.findById(menuCreateDTO.getButcheryCategoryId());

        if (butcheryOptional.isEmpty()) {
            return null;
        }

        ButcheryCategory butcheryCategory = butcheryOptional.get();

        Optional<Category> categoryOptional = categoryRepository.findById(menuCreateDTO.getCategoryId());

        if (categoryOptional.isEmpty()) {
            return null;
        }

        Category category = categoryOptional.get();

        newMenu.setButcheryCategoryId(butcheryCategory.getId());
        newMenu.setCategoryId(category.getId());

        newMenu = menuFileUploadService.uploadImage(menuCreateDTO.getImage(), newMenu);

        return menuRepository.save(newMenu);
    }

    /**
     * Updates an existing menu.
     *
     * @param menuUpdateDTO the data transfer object containing menu update data
     * @return the updated menu, or null if the menu, butchery category, or category is not found
     */
    @Override
    public Menu updateMenu(MenuUpdateDTO menuUpdateDTO) {

        Optional<Menu> menuOptional = menuRepository.findById(menuUpdateDTO.getId());

        if (menuOptional.isEmpty()) {
            return null;
        }

        Optional<ButcheryCategory> butcheryCategoryOptional = butcheryCategoryRepository.findById(menuUpdateDTO.getButcheryCategoryId());

        if (butcheryCategoryOptional.isEmpty()) {
            return null;
        }

        Optional<Category> categoryOptional = categoryRepository.findById(menuUpdateDTO.getCategoryId());

        if (categoryOptional.isEmpty()) {
            return null;
        }

        Menu menuUpdate = menuOptional.get();
        ButcheryCategory butcheryCategory = butcheryCategoryOptional.get();
        Category category = categoryOptional.get();

        menuUpdate.setName(menuUpdateDTO.getName());
        menuUpdate.setIsWholeAnimal(menuUpdateDTO.getIsWholeAnimal());
        menuUpdate.setWeight(menuUpdateDTO.getWeight());
        menuUpdate.setPrice(menuUpdateDTO.getPrice());
        menuUpdate.setDescription(menuUpdateDTO.getDescription());
        menuUpdate.setButcheryCategoryId(butcheryCategory.getId());
        menuUpdate.setCategoryId(category.getId());
        menuUpdate = menuFileUploadService.uploadImage(menuUpdateDTO.getImage(), menuUpdate);

        return menuRepository.save(menuUpdate);
    }

    /**
     * Deletes a menu by its ID.
     *
     * @param id the ID of the menu to delete
     */
    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    /**
     * Retrieves a list of menus by butchery category ID.
     *
     * @param id the ID of the butchery category
     * @return a list of menus belonging to the specified butchery category
     */
    @Override
    public List<Menu> getMenuListByButcheryCategoryId(Long id) {
        return menuRepository.getMenusByButcheryCategoryId(id);
    }
}
