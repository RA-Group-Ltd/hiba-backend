package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.ButcheryCategoryCreateDTO;
import kz.wave.hiba.DTO.UserDTO;
import kz.wave.hiba.Entities.ButcheryCategory;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ButcherService;
import kz.wave.hiba.Service.ButcheryCategoryService;
import kz.wave.hiba.Service.ButcheryService;
import kz.wave.hiba.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/butcheryCategory")
@RequiredArgsConstructor
public class ButcheryCategoryController {

    private final ButcheryCategoryService butcheryCategoryService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ButcherService butcherService;

    @GetMapping(value = "/{butcheryId}")
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

    @GetMapping(value = "/getCategoriesByOwner")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCategoriesByOwner(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);

            Long butcheryId = butcherService.getButcheryIdByButhcerUserId(user.getId());

            List<ButcheryCategory> butcheryCategories = butcheryCategoryService.getCategoriesByButcheryId(butcheryId);
            return new ResponseEntity<>(butcheryCategories, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
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

    @PostMapping(value = "/createButcheryCategoryByOwner")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_BUTCHER')")
    public ResponseEntity<?> createButcheryCategoryByOwner(@RequestBody ButcheryCategoryCreateDTO butcheryCategoryCreateDTO, HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);

            Long butcheryId = butcherService.getButcheryIdByButhcerUserId(user.getId());
            butcheryCategoryCreateDTO.setButcheryId(butcheryId);
            butcheryCategoryService.createButcheryCategory(butcheryCategoryCreateDTO);
            return new ResponseEntity<>("Butchery category successful created!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

}
