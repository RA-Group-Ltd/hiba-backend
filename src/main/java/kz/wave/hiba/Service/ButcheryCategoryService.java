package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.ButcheryCategoryCreateDTO;
import kz.wave.hiba.Entities.ButcheryCategory;

import java.util.List;

public interface ButcheryCategoryService {

    void createButcheryCategory(ButcheryCategoryCreateDTO butcheryCategoryCreateDTO);
    List<ButcheryCategory> getCategoriesByButcheryId(Long butcheryId);

}
