package kz.wave.hiba.Controller;

import kz.wave.hiba.DTO.*;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.Country;
import kz.wave.hiba.Entities.Region;
import kz.wave.hiba.Repository.RegionRepository;
import kz.wave.hiba.Service.CityService;
import kz.wave.hiba.Service.CountryService;
import kz.wave.hiba.Service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RegionRepository regionRepository;

    @GetMapping(value = "/getAllCountries")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping(value = "/getOneCountry/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Country> getOneCountry(@PathVariable(name = "id") Long id) {
        if (countryService.getCountryById(id) != null) {
            Country country = countryService.getCountryById(id);
            return ResponseEntity.ok(country);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/createCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> createCountry(@RequestBody CountryDTO countryDTO) {
        Country country = countryService.createCountry(countryDTO);

        if (country != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updateCountry/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> updateCountry(@RequestBody Country country) {
        if (countryService.updateCountry(country) != null) {
            return ResponseEntity.ok().body("Country updated!");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/deleteCountry/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    @GetMapping(value = "/getAllRegions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @GetMapping(value = "/getOneRegion/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneRegion(@PathVariable Long id) {
        if (regionService.getOneRegion(id) != null) {
            Region region = regionService.getOneRegion(id);
            return ResponseEntity.ok(region);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/createRegion")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> createRegion(@RequestBody RegionCreateDTO regionCreateDTO) {
        Country country = countryService.getCountryById(regionCreateDTO.getCountryId());
        if (country == null) {
            return new ResponseEntity<>("Country not found", HttpStatus.BAD_REQUEST);
        }

        Region region = regionService.createRegion(regionCreateDTO, country);

        if (region != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updateRegion/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> updateRegion(@RequestBody RegionUpdateDTO regionUpdateDTO) {
        if (regionService.updateRegion(regionUpdateDTO) != null) {
            return ResponseEntity.ok().body("Country updated!");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/deleteRegion/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public void deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    @GetMapping(value = "/getAllCities")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping(value = "/getOneCity/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneCity(@PathVariable Long id) {
        if (cityService.getOneCity(id) != null) {
            City city = cityService.getOneCity(id);
            return ResponseEntity.ok(city);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/createCity")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> createCity(@RequestBody CityCreateDTO cityCreateDTO) {
        Country country = countryService.getCountryById(cityCreateDTO.getCountryId());
        Region region = null;
        if (country == null) {
            return new ResponseEntity<>("Country not found", HttpStatus.BAD_REQUEST);
        }
        if (cityCreateDTO.getRegionId() != null ) {
            region = regionService.getOneRegion(cityCreateDTO.getRegionId());
            if(region == null){
                return new ResponseEntity<>("Region not found", HttpStatus.BAD_REQUEST);
            }
        }

        City city = cityService.createCity(cityCreateDTO, country, region);

        if (city != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updateCity/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> updateCity(@RequestBody CityUpdateDTO cityUpdateDTO) {
        Country country = countryService.getCountryById(cityUpdateDTO.getCountryId());
        Region region = regionService.getOneRegion(cityUpdateDTO.getRegionId());

        if (country == null) {
            return new ResponseEntity<>("Country not found", HttpStatus.BAD_REQUEST);
        }
        if (region == null) {
            return new ResponseEntity<>("Region not found", HttpStatus.BAD_REQUEST);
        }

        if (cityService.updateCity(cityUpdateDTO, country, region) != null) {
            return ResponseEntity.ok().body("City updated!");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/deleteCity/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }

}
