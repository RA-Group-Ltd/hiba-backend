package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.CountryDTO;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Repository.CountryRepository;
import kz.wave.hiba.Service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountryById(Long id) {
        if (countryRepository.findById(id).isPresent()) {
            return countryRepository.findById(id).orElseThrow();
        } else {
            return null;
        }
    }

    @Override
    public Country createCountry(CountryDTO countryDTO) {
        if (countryRepository.findByName(countryDTO.getName()) != null) {
            return null;
        }

        Country newCountry = new Country();
        newCountry.setName(countryDTO.getName());

        return countryRepository.save(newCountry);
    }

    @Override
    public Country updateCountry(Country country) {
        if (countryRepository.findById(country.getId()).isPresent()) {
            return countryRepository.save(country);
        } else {
            return null;
        }
    }

    @Override
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }
}
