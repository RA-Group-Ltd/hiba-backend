package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Repository.ButcheryRepository;
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
    private ButcheryRepository butcheryRepository;

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

        Optional<Butchery> butcheryOptional = butcheryRepository.findById(menu.getButcheryId());

        /*if (!butcheryOptional.isPresent()) {
            return null;
        }*/

        Butchery butchery = butcheryOptional.get();

        Optional<Category> categoryOptional = categoryRepository.findById(menu.getCategoryId());

        /*if (!categoryOptional.isEmpty()) {
            return null;
        }*/
        Category category = categoryOptional.get();

        newMenu.setButcheryId(butchery.getId());
        newMenu.setButcheryId(category.getId());

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
