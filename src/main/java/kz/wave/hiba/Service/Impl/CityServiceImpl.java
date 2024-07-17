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

/**
 * Implementation of the {@link CityService} interface.
 */
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    /**
     * Retrieves all cities.
     *
     * @return a list of all cities
     */
    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Retrieves a city by its ID.
     *
     * @param id the ID of the city to retrieve
     * @return the retrieved city
     * @throws java.util.NoSuchElementException if no city with the given ID is found
     */
    @Override
    public City getOneCity(Long id) {
        return cityRepository.findById(id).orElseThrow();
    }

    /**
     * Creates a new city.
     *
     * @param cityCreateDTO the data transfer object containing city creation data
     * @param country the country to which the city belongs
     * @param region the region to which the city belongs
     * @return the created city
     */
    @Override
    public City createCity(CityCreateDTO cityCreateDTO, Country country, Region region) {
        City city = new City();
        city.setName(cityCreateDTO.getName());
        city.setRegion(region);
        city.setCountry(country);

        return cityRepository.save(city);
    }

    /**
     * Updates an existing city.
     *
     * @param cityUpdateDTO the data transfer object containing city update data
     * @param country the country to which the city belongs
     * @param region the region to which the city belongs
     * @return the updated city, or null if no city with the given ID is found
     */
    @Override
    public City updateCity(CityUpdateDTO cityUpdateDTO, Country country, Region region) {
        Optional<City> city = cityRepository.findById(cityUpdateDTO.getId());

        if (!city.isPresent()) {
            return null;
        }

        City updateCity = city.get();
        updateCity.setCountry(country);
        updateCity.setRegion(region);
        updateCity.setName(cityUpdateDTO.getName());

        return cityRepository.save(updateCity);
    }

    /**
     * Deletes a city by its ID.
     *
     * @param id the ID of the city to delete
     */
    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
