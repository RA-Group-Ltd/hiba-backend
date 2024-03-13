package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.AddressCreateDTO;
import kz.wave.hiba.DTO.AddressUpdateDTO;
import kz.wave.hiba.Entities.Address;
import kz.wave.hiba.Entities.User;

import java.util.List;

public interface AddressService {

    List<Address> getMyAddresses(User user);
    Address getOneMyAddress(Long id);
    Address createMyAddress(AddressCreateDTO addressCreateDTO, User user);
    Address updateMyAddress(AddressUpdateDTO addressUpdateDTO, User user);
    void deleteMyAddress(Long id);

}
