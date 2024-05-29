package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.MailingUtils;
import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Response.ButcheryCategoryResponse;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Service.*;
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
public class ButcheryServiceImpl implements ButcheryService {

    private final ButcheryRepository butcheryRepository;
    private final ButcheryCategoryService butcheryCategoryService;
    private final MenuService menuService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ButcheryFileUploadCertificate butcheryFileUploadCertificate;
    private final ButcherRepository butcherRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailingUtils mailingUtils;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Override
    public List<Butchery> getAllButchery() {
        return butcheryRepository.findAll();
    }

    @Override
    public ButcheryResponse getOneButchery(Long id) {
        Butchery butchery = butcheryRepository.findById(id).orElseThrow();
        ButcheryResponse butcheryResponse = new ButcheryResponse();
        List<ButcheryCategory> butcheryCategories = butcheryCategoryService.getCategoriesByButcheryId(butchery.getId());
        List<ButcheryCategoryResponse> cats = new ArrayList<>();
        for (int i = 0; i < butcheryCategories.size(); i++) {
            ButcheryCategory butcheryCategory = butcheryCategories.get(i);
            List<Menu> menuItems = menuService.getMenuListByButcheryCategoryId(butcheryCategory.getId());
            Category category = categoryService.getCategoryById(butcheryCategory.getCategoryId());

            ButcheryCategoryResponse butcheryCategoryResponse = new ButcheryCategoryResponse(butcheryCategory.getId(), category, menuItems);
            cats.add(butcheryCategoryResponse);
        }
        butcheryResponse.setButchery(butchery);
        butcheryResponse.setCategories(cats);

        return butcheryResponse;
    }

    @Override
    public Butchery createButchery(ButcheryCreateDTO butcheryCreateDTO, City city) {
        System.out.println(butcheryCreateDTO.getPhone());
        Butchery newButchery = new Butchery();

        if (userRepository.findByPhone(butcheryCreateDTO.getPhone()) == null) {
            String genPassword = generatePassword();

            User user = new User();
            user.setName(butcheryCreateDTO.getOwner());
            user.setPhone(butcheryCreateDTO.getPhone());
            user.setCreatedAt(Instant.now());
            user.setPassword(passwordEncoder.encode(genPassword));
            user.setEmail(butcheryCreateDTO.getEmail());

            User newUser = userRepository.save(user);

            Role role = roleRepository.findByName("ROLE_BUTCHER");
            UserRoleId userRoleId = new UserRoleId(user.getId(), role.getId());
            UserRole userRole = new UserRole(userRoleId, user, role);
            userRoleRepository.save(userRole);


            mailingUtils.sendPass(butcheryCreateDTO.getEmail(), genPassword);

            newButchery.setOwner(newUser);
        } else {
            User user = userRepository.findByPhone(butcheryCreateDTO.getPhone());

            newButchery.setOwner(user);
        }

        newButchery.setName(butcheryCreateDTO.getName());
        newButchery.setAddress(butcheryCreateDTO.getAddress());
        if (butcheryCreateDTO.getLongitude() != null) {
            newButchery.setLongitude(butcheryCreateDTO.getLongitude());
        }
        if (butcheryCreateDTO.getLatitude() != null) {
            newButchery.setLatitude(butcheryCreateDTO.getLatitude());
        }
        newButchery.setCity(city);
        newButchery.setEmail(butcheryCreateDTO.getEmail());
        newButchery.setMeatType(butcheryCreateDTO.getMeatType());
        newButchery.setRegNumber(butcheryCreateDTO.getRegNumber());
        newButchery.setCreatedAt(Instant.now());
        butcheryFileUploadCertificate.uploadDocuments(butcheryCreateDTO.getDocuments(), newButchery);

        return butcheryRepository.save(newButchery);
    }

    @Override
    public Butchery updateButchery(ButcheryUpdateDTO butcheryUpdateDTO, City city) {
        Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryUpdateDTO.getId());

        if (butcheryOptional.isPresent()) {
            Butchery updateButchery = butcheryOptional.get();

            updateButchery.setName(butcheryUpdateDTO.getName());
            updateButchery.setAddress(butcheryUpdateDTO.getAddress());
            updateButchery.setLongitude(butcheryUpdateDTO.getLongitude());
            updateButchery.setLatitude(butcheryUpdateDTO.getLatitude());
            updateButchery.setCity(city);

            butcheryFileUploadCertificate.uploadDocuments(butcheryUpdateDTO.getDocuments(), updateButchery);

            return butcheryRepository.save(updateButchery);
        } else {
            return null;
        }
    }

    @Override
    public void deleteButchery(Long id) {
        butcheryRepository.deleteById(id);
    }

    @Override
    public Long quantityOfButcheries() {
        return butcheryRepository.countButcheries();
    }

    @Override
    public ButcheryResponse getButcheryInfoById(Long id) {

        ButcheryResponse butcheryResponse = getOneButchery(id);

        List<User> employees = butcherRepository.findAllUsersByButchery(butcheryResponse.getButchery());

        int deliveredOrders = orderRepository.getDeliveredOrdersByButchery(butcheryResponse.getButchery());
        int activeOrders = orderRepository.getActiveOrdersByButchery(butcheryResponse.getButchery());

        butcheryResponse.setEmployees(employees);
        butcheryResponse.setActiveOrders(activeOrders);
        butcheryResponse.setDeliveredOrders(deliveredOrders);

        return butcheryResponse;
    }

    @Override
    public List<Butchery> getButcheries(String sort, String filter, String query) {
        List<Long> cityList = new ArrayList<>();
        if(!filter.isEmpty()){
            String[] country_cities = filter.split(";");
            for(String country_city : country_cities)       {
                String[] country_and_cities = country_city.split(":");
                if (country_and_cities.length > 1){
                    String countryName = country_and_cities[0];
                    String[] cityNames = country_and_cities[1].split(",");
                    if(cityNames.length > 0){
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
        return butcheryRepository.findButcheries(sort, query, cityList);
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
