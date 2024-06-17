package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.PromotionCreateDTO;
import kz.wave.hiba.DTO.PromotionUpdateDTO;
import kz.wave.hiba.Entities.Promotion;
import kz.wave.hiba.Repository.PromotionRepository;
import kz.wave.hiba.Service.PromotionService;
import kz.wave.hiba.Service.PromotionUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionUploadService promotionUploadService;

    @Override
    public List<Promotion> getPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion getPromotion(Long id) {
        return promotionRepository.findById(id).orElseThrow();
    }

    @Override
    public Promotion createPromotion(PromotionCreateDTO promotionCreateDTO) {
        Optional<Promotion> promotionOptional = Optional.ofNullable(promotionRepository.findPromotionByTitle(promotionCreateDTO.getTitle()));

        if (promotionOptional.isEmpty()) {
            Promotion newPromotion = new Promotion();
            newPromotion.setCreatedAt(Instant.now());
            newPromotion.setTitle(promotionCreateDTO.getTitle());
            newPromotion.setDescription(promotionCreateDTO.getDescription());
            newPromotion.setAudience(promotionCreateDTO.getAudience());

            promotionUploadService.uploadImage(promotionCreateDTO.getImage(), newPromotion);

            return promotionRepository.save(newPromotion);
        } else {
            return null;
        }
    }

    @Override
    public Promotion updatePromotion(PromotionUpdateDTO promotionUpdateDTO) {
        System.out.println(promotionUpdateDTO.getId());
        Optional<Promotion> promotionOptional = promotionRepository.findById(promotionUpdateDTO.getId());

        if (promotionOptional.isEmpty()) {
            return null;
        }

        Promotion updatePromotion = promotionOptional.get();
        updatePromotion.setDescription(promotionUpdateDTO.getDescription());
        updatePromotion.setTitle(promotionUpdateDTO.getTitle());
        updatePromotion.setTitle(promotionUpdateDTO.getTitle());
        updatePromotion.setAudience(promotionUpdateDTO.getAudience());


        return promotionRepository.save(updatePromotion);
    }

    @Override
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }
}
