package kz.wave.hiba.Controller;

import kz.wave.hiba.DTO.ButcheryCategoryCreateDTO;
import kz.wave.hiba.Entities.ButcheryCategory;
import kz.wave.hiba.Service.ButcheryCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/butchery-category")
public class ButcheryCategoryController {

    @Autowired
    private ButcheryCategoryService butcheryCategoryService;

    @GetMapping(value = "/getCategoriesByButcheryId/{butcheryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCategoriesByButcheryId(@RequestParam Long butcheryId) {
        try {
            List<ButcheryCategory> butcheryCategories = butcheryCategoryService.getCategoriesByButcheryId(butcheryId);
            return new ResponseEntity<>(butcheryCategories, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/createButcheryCategory")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_BUTCHER')")
    public ResponseEntity<?> createButcheryCategory(@RequestBody ButcheryCategoryCreateDTO butcheryCategoryCreateDTO) {
        try {
            butcheryCategoryService.createButcheryCategory(butcheryCategoryCreateDTO);
            return new ResponseEntity<>("Butchery category successful created!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

}
