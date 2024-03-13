package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.AddressCreateDTO;
import kz.wave.hiba.DTO.AddressUpdateDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.AddressService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/getAllMyAddresses")
    public ResponseEntity<?> getAllMyAddresses(HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        return ResponseEntity.ok(addressService.getMyAddresses(user));
    }

    @GetMapping(value = "/getOneMyAddress/{id}")
    public ResponseEntity<?> getOneMyAddress(HttpServletRequest request) {
        try {
            String userToken = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(userToken);
            User user = userRepository.findByPhone(currentUser);

            return ResponseEntity.ok(addressService.getOneMyAddress(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Don't find", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/addMyNewAddress")
    public ResponseEntity<?> addMyNewAddress(@RequestBody AddressCreateDTO addressCreateDTO, HttpServletRequest request) {
        try {
            String userToken = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(userToken);
            User user = userRepository.findByPhone(currentUser);

            addressService.createMyAddress(addressCreateDTO, user);

            return new ResponseEntity<>("Created address", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updateMyAddress")
    public ResponseEntity<?> updateMyAddress(@RequestBody AddressUpdateDTO addressUpdateDTO, HttpServletRequest request) {
        try {
            String userToken = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(userToken);
            User user = userRepository.findByPhone(currentUser);

            addressService.updateMyAddress(addressUpdateDTO, user);
            return new ResponseEntity<>("Address updated", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/deleteMyAddress/{id}")
    public void deleteMyAddress(@PathVariable Long id) {
        addressService.deleteMyAddress(id);
    }

}
