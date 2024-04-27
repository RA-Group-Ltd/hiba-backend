package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.MenuCreateDTO;
import kz.wave.hiba.Entities.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> getAllMenus();
    Menu getOneMenu(Long id);
    Menu createMenu(MenuCreateDTO menuCreateDTO);
    Menu updateMenu(Menu updateMenu);
    void deleteMenu(Long id);
//    List<Menu> getMenusByIds(List<Long> ids);
    List<Menu> getMenuListByButcheryCategoryId(Long id);

}
