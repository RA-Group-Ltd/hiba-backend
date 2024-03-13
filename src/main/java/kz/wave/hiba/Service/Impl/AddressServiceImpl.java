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
    public List<Address> getMyAddresses(User user) {
        return addressRepository.findAddressesByUserId(user.getId());
    }

    @Override
    public Address getOneMyAddress(Long id) {
        return addressRepository.findById(id).orElseThrow();
    }

    @Override
    public Address createMyAddress(AddressCreateDTO addressCreateDTO, User user) {
        Address address = new Address();

        Optional<User> userId = userRepository.findById(user.getId());
        Optional<City> cityId = cityRepository.findById(addressCreateDTO.getCity_id());

        address.setApartment(address.getApartment());
        address.setCity(cityId.get());
        address.setUser(userId.get());
        address.setFloor(addressCreateDTO.getFloor());
        address.setEntrance(addressCreateDTO.getEntrance());
        address.setBuilding_name(addressCreateDTO.getBuilding_name());

        return addressRepository.save(address);
    }

    @Override
    public Address updateMyAddress(AddressUpdateDTO addressUpdateDTO, User user) {
        Optional<Address> addressOptional = addressRepository.findById(addressUpdateDTO.getId());

        if (addressOptional.isEmpty()) {
            return null;
        }

        Address addressUpdate = addressOptional.get();

        Optional<City> cityId = cityRepository.findById(addressUpdateDTO.getCity_id());

        addressUpdate.setBuilding_name(addressUpdateDTO.getBuilding_name());
        addressUpdate.setApartment(addressUpdateDTO.getApartment());
        addressUpdate.setFloor(addressUpdateDTO.getFloor());
        addressUpdate.setEntrance(addressUpdateDTO.getEntrance());
        addressUpdate.setCity(cityId.get());

        return addressRepository.save(addressUpdate);
    }

    @Override
    public void deleteMyAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
