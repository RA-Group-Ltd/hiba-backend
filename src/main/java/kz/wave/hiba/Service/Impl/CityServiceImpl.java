package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.CityCreateDTO;
import kz.wave.hiba.DTO.CityUpdateDTO;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Entities.Region;
import kz.wave.hiba.Repository.CityRepository;
import kz.wave.hiba.Service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getOneCity(Long id) {
        return cityRepository.findById(id).orElseThrow();
    }

    @Override
    public City createCity(CityCreateDTO cityCreateDTO, Country country, Region region) {

        City city = new City();
        city.setName(cityCreateDTO.getName());
        city.setRegion(region);
        city.setCountry(country);

        return cityRepository.save(city);
    }

    @Override
    public City updateCity(CityUpdateDTO cityUpdateDTO, Country country, Region region) {
        Optional<City> city = cityRepository.findById(cityUpdateDTO.getId());

        if (city.isPresent()) {
            return null;
        }

        City updateCity = city.get();
        updateCity.setCountry(country);
        updateCity.setRegion(region);
        updateCity.setName(updateCity.getName());

        return cityRepository.save(updateCity);
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
