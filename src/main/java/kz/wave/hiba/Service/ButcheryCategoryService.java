package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.ButcheryCategoryCreateDTO;
import kz.wave.hiba.Entities.ButcheryCategory;

public interface ButcheryCategoryService {

    void createButcheryCategory(ButcheryCategoryCreateDTO butcheryCategoryCreateDTO);
    ButcheryCategory getCategoriesByButcheryId(Long butcheryId);

}
