package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.AddressCreateDTO;
import kz.wave.hiba.DTO.AddressUpdateDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllMyAddresses(HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        return addressService.getMyAddresses(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOneMyAddress(HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        return addressService.getOneMyAddress(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> addMyNewAddress(@RequestBody AddressCreateDTO addressCreateDTO, HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        return addressService.createMyAddress(addressCreateDTO, user);
    }

    @PutMapping
    public ResponseEntity<?> updateMyAddress(@RequestBody AddressUpdateDTO addressUpdateDTO, HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        return addressService.updateMyAddress(addressUpdateDTO, user);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteMyAddress(@PathVariable Long id) {
        addressService.deleteMyAddress(id);
    }

}
