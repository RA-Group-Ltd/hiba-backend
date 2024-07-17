package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.CountryDTO;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Repository.CountryRepository;
import kz.wave.hiba.Service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link CountryService} interface.
 */
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    /**
     * Retrieves all countries.
     *
     * @return a list of all countries
     */
    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    /**
     * Retrieves a country by its ID.
     *
     * @param id the ID of the country to retrieve
     * @return the retrieved country, or null if not found
     */
    @Override
    public Country getCountryById(Long id) {
        if (countryRepository.findById(id).isPresent()) {
            return countryRepository.findById(id).orElseThrow();
        } else {
            return null;
        }
    }

    /**
     * Creates a new country.
     *
     * @param countryDTO the data transfer object containing country creation data
     * @return the created country, or null if a country with the same name already exists
     */
    @Override
    public Country createCountry(CountryDTO countryDTO) {
        if (countryRepository.findByName(countryDTO.getName()) != null) {
            return null;
        }

        Country newCountry = new Country();
        newCountry.setName(countryDTO.getName());

        return countryRepository.save(newCountry);
    }

    /**
     * Updates an existing country.
     *
     * @param country the country to update
     * @return the updated country, or null if not found
     */
    @Override
    public Country updateCountry(Country country) {
        if (countryRepository.findById(country.getId()).isPresent()) {
            return countryRepository.save(country);
        } else {
            return null;
        }
    }

    /**
     * Deletes a country by its ID.
     *
     * @param id the ID of the country to delete
     */
    @Override
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }
}
