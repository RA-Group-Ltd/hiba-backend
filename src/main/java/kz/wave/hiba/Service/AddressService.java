package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.AddressCreateDTO;
import kz.wave.hiba.DTO.AddressUpdateDTO;
import kz.wave.hiba.Entities.Address;
import kz.wave.hiba.Entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    ResponseEntity<?> getMyAddresses(User user);
    ResponseEntity<?> getOneMyAddress(Long id);
    ResponseEntity<?> createMyAddress(AddressCreateDTO addressCreateDTO, User user);
    ResponseEntity<?> updateMyAddress(AddressUpdateDTO addressUpdateDTO, User user);
    ResponseEntity<?> deleteMyAddress(Long id);

}
