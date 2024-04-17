package kz.wave.hiba.Controller;

import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.DTO.UserDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Repository.CityRepository;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Service.ButcheryService;
import kz.wave.hiba.Service.CityService;
import kz.wave.hiba.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/butcheries")
public class ButcheryController {

    @Autowired
    private ButcheryService butcheryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getAllButcheries")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Butchery>> getAllButcheries() {
        List<Butchery> butcheries = butcheryService.getAllButchery();
        return ResponseEntity.ok(butcheries);
    }

    @GetMapping(value = "/getOneButchery/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ButcheryResponse> getOneButchery(@PathVariable Long id) {
        if (butcheryService.getOneButchery(id) != null) {
            ButcheryResponse butchery= butcheryService.getOneButchery(id);
            return ResponseEntity.ok(butchery);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/create-butchery")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> createButchery(@RequestBody ButcheryAndUserResponse butcheryAndUserResponse) {
        ButcheryCreateDTO butcheryCreateDTO = butcheryAndUserResponse.getButchery();
        UserDTO userDTO = butcheryAndUserResponse.getUser();
        Optional<City> cityOptional = cityRepository.findById(butcheryCreateDTO.getCityId());

        if (cityOptional.isEmpty()) {
            return new ResponseEntity<>("City not found", HttpStatus.BAD_REQUEST);
        }
        City city = cityOptional.get();

        Butchery butchery = butcheryService.createButchery(butcheryCreateDTO, city);
        userService.createUser(userDTO);

        if (butchery != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updateButchery")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> updateButchery(@RequestBody ButcheryUpdateDTO butcheryUpdateDTO) {
        try {
            City city = cityService.getOneCity(butcheryUpdateDTO.getCityId());

            if (city == null) {
                return ResponseEntity.badRequest().body("City not found");
            }

            Butchery updatedButchery = butcheryService.updateButchery(butcheryUpdateDTO, city);
            if (updatedButchery != null) {
                return ResponseEntity.ok().body("Butchery updated!");
            } else {
                return ResponseEntity.badRequest().body("Butchery not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/deleteButchery/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public void deleteButchery(@PathVariable Long id) {
        butcheryService.deleteButchery(id);
    }
}
