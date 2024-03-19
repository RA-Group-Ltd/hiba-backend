package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.ButcheryCategory;
import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Repository.ButcheryCategoryRepository;
import kz.wave.hiba.Repository.CategoryRepository;
import kz.wave.hiba.Repository.MenuRepository;
import kz.wave.hiba.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ButcheryCategoryRepository butcheryCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getOneMenu(Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    @Override
    public Menu createMenu(Menu menu) {

        Menu newMenu = new Menu();
        newMenu.setName(menu.getName());
        newMenu.setIsWholeAnimal(menu.getIsWholeAnimal());
        newMenu.setWeight(menu.getWeight());

        Optional<ButcheryCategory> butcheryOptional = butcheryCategoryRepository.findById(menu.getButcheryCategoryId());

        if (butcheryOptional.isEmpty()) {
            return null;
        }

        ButcheryCategory butcheryCategory = butcheryOptional.get();

        Optional<Category> categoryOptional = categoryRepository.findById(menu.getCategoryId());

        if (categoryOptional.isEmpty()) {
            return null;
        }

        Category category = categoryOptional.get();

        newMenu.setButcheryCategoryId(butcheryCategory.getId());
        newMenu.setCategoryId(category.getId());

        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(Menu updateMenu) {
        return menuRepository.save(updateMenu);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
