package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.MenuCreateDTO;
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


    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getOneMenu(Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    @Override
    public Menu createMenu(MenuCreateDTO menuCreateDTO) {

        Menu newMenu = new Menu();
        newMenu.setName(menuCreateDTO.getName());
        newMenu.setIsWholeAnimal(menuCreateDTO.getIsWholeAnimal());
        newMenu.setWeight(menuCreateDTO.getWeight());
        newMenu.setPrice(menuCreateDTO.getPrice());

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

    @Override
    public Menu updateMenu(Menu updateMenu) {
        return menuRepository.save(updateMenu);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    /*@Override
    public List<Menu> getMenusByIds(List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {
            menuRepository.getMenuById(i);
        }
    }*/

    @Override
    public List<Menu> getMenuListByButcheryCategoryId(Long id) {
        return menuRepository.getMenusByButcheryCategoryId(id);
    }
}
