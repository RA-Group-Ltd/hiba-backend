package kz.wave.hiba.Controller;

import kz.wave.hiba.DTO.PromotionCreateDTO;
import kz.wave.hiba.DTO.PromotionUpdateDTO;
import kz.wave.hiba.Entities.Promotion;
import kz.wave.hiba.Service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/promotion")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping(value = "/getPromotions")
    public ResponseEntity<?> getPromotions() {
        try {
            List<Promotion> promotionList = promotionService.getPromotions();
            return new ResponseEntity<>(promotionList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getPromotion/{id}")
    public ResponseEntity<?> getPromotion(@PathVariable Long id) {
        try {
            Promotion promotion = promotionService.getPromotion(id);
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Id didn't find in database", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/createPromotion")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> createPromotion(@ModelAttribute PromotionCreateDTO promotionCreateDTO) {
        try {
            promotionService.createPromotion(promotionCreateDTO);
            return new ResponseEntity<>("Promotion created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Promotion name already exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updatePromotion")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> updatePromotion(@ModelAttribute PromotionUpdateDTO promotionUpdateDTO) {
        try {
            promotionService.updatePromotion(promotionUpdateDTO);
            return new ResponseEntity<>("Promotion updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Promotion id didn't find in database", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/deletePromotion/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> deletePromotion(@PathVariable Long id) {
        try {
            promotionService.deletePromotion(id);
            return new ResponseEntity<>("Promotion deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Promotion's id didn't find in database", HttpStatus.BAD_REQUEST);
        }
    }

}
