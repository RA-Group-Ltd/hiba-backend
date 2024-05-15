package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.AddressCreateDTO;
import kz.wave.hiba.DTO.AddressUpdateDTO;
import kz.wave.hiba.Entities.Address;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.AddressRepository;
import kz.wave.hiba.Repository.CityRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> getMyAddresses(User user) {
        try {
            List<Address> addressList = addressRepository.findAddressesByUserId(user.getId());
            return new ResponseEntity<> (addressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Addresses didn't find!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> getOneMyAddress(Long id) {
        try {
            Address address = addressRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Address by id didn't find!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> createMyAddress(AddressCreateDTO addressCreateDTO, User user) {
        try {
            Address address = new Address();

            Optional<User> userId = userRepository.findById(user.getId());
            Optional<City> cityId = cityRepository.findById(addressCreateDTO.getCity_id());

            address.setApartment(addressCreateDTO.getApartment());
            address.setCity(cityId.get());
            address.setUser(userId.get());
            address.setFloor(addressCreateDTO.getFloor());
            address.setEntrance(addressCreateDTO.getEntrance());
            address.setBuilding(addressCreateDTO.getBuilding());
            address.setAddress(addressCreateDTO.getAddress());
            address.setName(addressCreateDTO.getName());

            return new ResponseEntity<>(addressRepository.save(address), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Address doesn't created", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> updateMyAddress(AddressUpdateDTO addressUpdateDTO, User user) {
        try {
            Optional<Address> addressOptional = addressRepository.findById(addressUpdateDTO.getId());

            if (addressOptional.isEmpty()) {
                return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
            }

            Address addressUpdate = addressOptional.get();

            Optional<City> cityId = cityRepository.findById(addressUpdateDTO.getCity_id());

            addressUpdate.setBuilding(addressUpdateDTO.getBuilding());
            addressUpdate.setName(addressUpdateDTO.getName());
            addressUpdate.setAddress(addressUpdateDTO.getAddress());
            addressUpdate.setApartment(addressUpdateDTO.getApartment());
            addressUpdate.setFloor(addressUpdateDTO.getFloor());
            addressUpdate.setEntrance(addressUpdateDTO.getEntrance());
            addressUpdate.setCity(cityId.get());

            return new ResponseEntity<>(addressRepository.save(addressUpdate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Address didn't updated", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> deleteMyAddress(Long id) {
        try {
            addressRepository.deleteById(id);
            return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Address by id didn't find!", HttpStatus.NOT_FOUND);
        }
    }
}
