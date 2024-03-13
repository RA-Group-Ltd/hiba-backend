package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.CityCreateDTO;
import kz.wave.hiba.DTO.CityUpdateDTO;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Entities.Region;

import java.util.List;

public interface CityService {

    List<City> getAllCities();
    City getOneCity(Long id);
    City createCity(CityCreateDTO cityCreateDTO, Country country, Region region);
    City updateCity(CityUpdateDTO cityUpdateDTO, Country country, Region region);
    void deleteCity(Long id);

}
