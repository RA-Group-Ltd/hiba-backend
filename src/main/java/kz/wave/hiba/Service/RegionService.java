package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.RegionCreateDTO;
import kz.wave.hiba.DTO.RegionUpdateDTO;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Entities.Region;

import java.util.List;

public interface RegionService {

    Region createRegion(RegionCreateDTO regionCreateDTO, Country country);
    Region updateRegion(RegionUpdateDTO regionUpdateDTO);
    List<Region> getAllRegions();
    Region getOneRegion(Long id);
    void deleteRegion(Long id);

}
