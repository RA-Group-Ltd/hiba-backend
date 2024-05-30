package kz.wave.hiba.Controller;

import com.google.api.Http;
import kz.wave.hiba.DTO.MenuCreateDTO;
import kz.wave.hiba.DTO.MenuUpdateDTO;
import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping(value = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllMenus() {
        try {
            List<Menu> menus = menuService.getAllMenus();
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMenusByButcheryCategoryId(@PathVariable(name = "id") Long id) {
        try {
            List<Menu> menus = menuService.getMenuListByButcheryCategoryId(id);
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneMenuById(@PathVariable Long id) {
        try {
            Menu menu = menuService.getOneMenu(id);
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> createMenu(@ModelAttribute MenuCreateDTO menuCreateDTO) {
        try {
            Menu createMenu = menuService.createMenu(menuCreateDTO);
            return new ResponseEntity<>(createMenu, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("You don't have butchery role!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_BUTHCER')")
    public ResponseEntity<?> updateMenu(@RequestBody MenuUpdateDTO menuUpdateDTO) {
        try {
            Menu updateMenu = menuService.updateMenu(menuUpdateDTO);
            return new ResponseEntity<>(updateMenu, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("You don't have buchery role!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER')")
    public void deleteMenu(@PathVariable Long id) {
        try {
            menuService.deleteMenu(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
