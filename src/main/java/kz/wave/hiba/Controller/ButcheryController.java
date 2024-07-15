package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Response.ButcheryAndUserResponse;
import kz.wave.hiba.Response.ButcheryOrderStats;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Service.ButcheryService;
import kz.wave.hiba.Service.CityService;
import kz.wave.hiba.Service.UserService;
import kz.wave.hiba.Specification.ButcherySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private ButcheryRepository butcheryRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CountryRepository countryRepository;


    @GetMapping(value = "/getAllButcheries")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Butchery>> getAllButcheries() {
        List<Butchery> butcheries = butcheryService.getAllButchery();
        return ResponseEntity.ok(butcheries);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER', 'ROLE_BUTCHERY_EMPLOYEE')")
    public ResponseEntity<ButcheryOrderStats> getButcheryOrderStats(HttpServletRequest request) {
        try {
            String token =  jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);
            Butchery butchery = butcheryRepository.findButcheryByOwner(user);
            ButcheryOrderStats butcheryOrderStats = butcheryService.getOrderStat(butchery);

            return new ResponseEntity<>(butcheryOrderStats, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();

        }
    }

    @GetMapping(value = "/getByButcheryOwner")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ButcheryResponse> getByButcheryOwner(HttpServletRequest request){
        try {
            String token =  jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);
            Butchery butchery = butcheryRepository.findButcheryByOwner(user);

            ButcheryResponse butcheryResponse = butcheryService.getButcheryInfoById(butchery.getId());

            return new ResponseEntity<>(butcheryResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping(value = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> searchButcheries(@RequestParam(value = "q", required = false) String q,
                                              @RequestParam(value = "categories", required = false) Integer[] categories,
                                              @RequestParam(value = "latitude", required = false) Float latitude,
                                              @RequestParam(value = "longitude", required = false) Float longitude,
                                              @RequestParam(value = "sort", defaultValue = "name") String sort) {

        Specification<Butchery> spec = Specification.where(ButcherySpecification.hasNameLike(q))
                .and(ButcherySpecification.isCategoryIn(categories));

        Sort sortOrder = sort.equalsIgnoreCase("name") ? Sort.by("name").descending() : Sort.unsorted();

        List<Butchery> result = butcheryRepository.findAll(spec, sortOrder);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ButcheryResponse> getOneButchery(@PathVariable Long id) {
        if (butcheryService.getOneButchery(id) != null) {
            ButcheryResponse butchery= butcheryService.getOneButchery(id);
            return ResponseEntity.ok(butchery);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> createButchery(@RequestBody ButcheryAndUserResponse butcheryAndUserResponse) {
        try {
            ButcheryCreateDTO butcheryCreateDTO = butcheryAndUserResponse.getButchery();
//            UserDTO userDTO = butcheryAndUserResponse.getUser();
            Optional<City> cityOptional = cityRepository.findById(butcheryCreateDTO.getCityId());


            if (cityOptional.isEmpty()) {
                return new ResponseEntity<>("City not found", HttpStatus.NOT_FOUND);
            }


            City city = cityOptional.get();

            butcheryService.createButchery(butcheryCreateDTO, city);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER')")
    public ResponseEntity<?> updateButchery(@ModelAttribute ButcheryUpdateDTO butcheryUpdateDTO) {
        try {
            City city = cityService.getOneCity(butcheryUpdateDTO.getCity());

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

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public void deleteButchery(@PathVariable Long id) {
        butcheryService.deleteButchery(id);
    }
}
