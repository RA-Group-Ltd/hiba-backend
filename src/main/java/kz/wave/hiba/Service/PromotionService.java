package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.PromotionCreateDTO;
import kz.wave.hiba.DTO.PromotionUpdateDTO;
import kz.wave.hiba.Entities.Promotion;

import java.util.List;

public interface PromotionService {

    List<Promotion> getPromotions();
    Promotion getPromotion(Long id);
    Promotion createPromotion(PromotionCreateDTO promotionCreateDTO);
    Promotion updatePromotion(PromotionUpdateDTO promotionUpdateDTO);
    void deletePromotion(Long id);

}
