package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.CountryDTO;
import kz.wave.hiba.Entities.Country;

import java.util.List;

public interface CountryService {

    List<Country> getAllCountries();
    Country getCountryById(Long id);
    Country createCountry(CountryDTO countryDTO);
    Country updateCountry(Country country);
    void deleteCountry(Long id);

}
