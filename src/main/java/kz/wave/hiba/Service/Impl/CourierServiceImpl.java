package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.MailingUtils;
import kz.wave.hiba.DTO.CourierCreateDTO;
import kz.wave.hiba.DTO.CourierUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Response.CourierOrderResponse;
import kz.wave.hiba.Response.CourierResponse;
import kz.wave.hiba.Service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailingUtils mailingUtils;
    private final CountryRepository countryRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    @Override
    public CourierResponse getCourier(Long id) {
        Courier courier = courierRepository.findById(id).orElseThrow();
        Long actOrders = orderRepository.countActiveOrdersByCourierId(id);
        Long delOrders = orderRepository.countDeliveredOrdersByCourierId(id);

        return new CourierResponse(courier, delOrders, actOrders);
    }

    @Override
    public Courier getCourierByFullNameOrPhoneOrEmail(String userName, String userPhone) {
        return courierRepository.findCourierByUserNameOrUserPhone(userName, userPhone);
    }

    @Override
    public Courier createCourier(CourierCreateDTO courierCreateDTO) {
        Courier newCourier = new Courier();

        // Check if user with the given phone already exists
        User existingUser = userRepository.findByPhone(courierCreateDTO.getPhone());
        if (existingUser == null) {
            String newPassword = generatePassword();

            // Create a new User
            User user = new User();
            user.setName(courierCreateDTO.getName());
            user.setPhone(courierCreateDTO.getPhone());
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setEmail(courierCreateDTO.getEmail());
            user.setCreatedAt(Instant.now());

            // Save the user to generate ID
            User newUser = userRepository.save(user);

            // Assign ROLE_COURIER to the user
            Role role = roleRepository.findByName("ROLE_COURIER");
            UserRoleId userRoleId = new UserRoleId(newUser.getId(), role.getId());
            UserRole userRole = new UserRole(userRoleId, newUser, role);
            userRoleRepository.save(userRole);

            mailingUtils.sendPass(courierCreateDTO.getEmail(), newPassword);

            // Assign the saved user to the courier
            newCourier.setUser(newUser);
        } else {
            // User already exists, assign the existing user to the courier
            newCourier.setUser(existingUser);
        }

        // Set the city for the courier
        Optional<City> cityOptional = cityRepository.findById(courierCreateDTO.getCityId());
        if (cityOptional.isEmpty()) {
            return null;
        }
        newCourier.setCity(cityOptional.get());
        newCourier.setCreatedAt(Instant.now());

        // Save and return the courier
        return courierRepository.save(newCourier);
    }

    @Override
    public Courier udpateCourier(CourierUpdateDTO courierUpdateDTO) {
        return null;
    }

    @Override
    public Courier getCourierByUserId(Long userId) {
        return courierRepository.findByUserId(userId);
    }

    @Override
    public void deleteCourierById(Long id) {
        courierRepository.deleteById(id);
    }

    @Override
    public List<CourierOrderResponse> getCouriers(String sort, String filter, String query) {
        List<Long> cityList = new ArrayList<>();
        if(!filter.isEmpty()) {
            String[] country_cities = filter.split(";");
            for(String country_city : country_cities) {
                String[] country_and_cities = country_city.split(":");
                if (country_and_cities.length > 1) {
                    String countryName = country_and_cities[0];
                    String[] cityNames = country_and_cities[1].split(",");
                    if(cityNames.length > 0) {
                        Country country = countryRepository.findByName(countryName);
                        List<Long> cities = cityRepository.findAllIdsByCountryAndNameInList(country, cityNames);
                        cityList.addAll(cities);
                    }
                }
            }
        }
        if (cityList.isEmpty()){
            cityList = null;
        }

        return courierRepository.findCouriers(query, sort, cityList);
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
