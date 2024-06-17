package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.ButcherEmployeeCreateDTO;
import kz.wave.hiba.Entities.Butcher;
import kz.wave.hiba.Entities.User;

public interface ButcherService {
    Long getButcheryIdByButhcerUserId(Long userId);

    Butcher addButcherEmployee(ButcherEmployeeCreateDTO butcherEmployeeCreateDTO, User currentUser);

    void deleteButcherByUserId(Long id, User butcheryOwner);
}
