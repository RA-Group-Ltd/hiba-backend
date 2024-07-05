package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.MailingUtils;
import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.ButcheryUpdateDTO;
import kz.wave.hiba.DTO.WorkingHourDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Enum.DayOfWeek;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Response.ButcheryCategoryResponse;
import kz.wave.hiba.Response.ButcheryOrderStats;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

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
    private final ButcheryFileUploadService butcheryFileUploadService;
    private final ButcherRepository butcherRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailingUtils mailingUtils;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final WorkingHoursRepository workingHoursRepository;

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
            Long categoryId = butcheryCategory.getCategoryId().getId();
            Category category = categoryService.getCategoryById(categoryId);

            ButcheryCategoryResponse butcheryCategoryResponse = new ButcheryCategoryResponse(butcheryCategory.getId(), category, menuItems);
            cats.add(butcheryCategoryResponse);
        }
        butcheryResponse.setButchery(butchery);
        butcheryResponse.setCategories(cats);
        butcheryResponse.setWorkingHours(workingHoursRepository.findAllByButcheryId(id));

        return butcheryResponse;
    }

    @Override
    public Butchery createButchery(ButcheryCreateDTO butcheryCreateDTO, City city) {
        Butchery newButchery = new Butchery();
        Butcher newButcher = new Butcher();

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
            newButcher.setUser(newUser);

        } else {
            User user = userRepository.findByPhone(butcheryCreateDTO.getPhone());
            newButcher.setUser(user);
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
        newButchery.setPhone(butcheryCreateDTO.getPhone());
        newButchery.setEmail(butcheryCreateDTO.getEmail());
        newButchery.setMeatType(butcheryCreateDTO.getMeatType());
        newButchery.setRegNumber(butcheryCreateDTO.getRegNumber());
        newButchery.setCreatedAt(Instant.now());
        butcheryFileUploadService.uploadDocuments(butcheryCreateDTO.getDocuments(), newButchery);

        Butchery butchery = butcheryRepository.save(newButchery);
        newButcher.setButchery(butchery);
        butcherRepository.save(newButcher);

        for(DayOfWeek day : DayOfWeek.values()){
            WorkingHours workingHours = new WorkingHours();
            workingHours.setButchery(butchery);
            workingHours.setDayOfWeek(day);
            if(day == DayOfWeek.SUNDAY){
                workingHours.setClosed(true);
            }else{
                workingHours.setOpenTime("00:00");
                workingHours.setCloseTime("00:00");
                workingHours.setClosed(false);
            }
            workingHoursRepository.save(workingHours);
        }

        return butchery;

    }

    @Override
    public Butchery updateButchery(ButcheryUpdateDTO butcheryUpdateDTO, City city) {
        Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryUpdateDTO.getId());

        if (butcheryOptional.isPresent()) {
            Butchery updateButchery = butcheryOptional.get();

            updateButchery.setAddress(butcheryUpdateDTO.getAddress());
            updateButchery.setCity(city);
            updateButchery.setPhone(butcheryUpdateDTO.getPhone());

            updateButchery = butcheryFileUploadService.uploadImage(butcheryUpdateDTO.getImage(), updateButchery);

            for(WorkingHourDTO workingHourDto : butcheryUpdateDTO.getWorkingHours()){
                WorkingHours existing = workingHoursRepository.findWorkingHoursByButcheryIdAndDayOfWeek(butcheryUpdateDTO.getId(), workingHourDto.getDayOfWeek());
                if(existing == null){
                    WorkingHours newWorkingHour = new WorkingHours();
                    newWorkingHour.setClosed(workingHourDto.isClosed());
                    newWorkingHour.setDayOfWeek(workingHourDto.getDayOfWeek());

                    if(workingHourDto.isClosed()){
                        newWorkingHour.setOpenTime(null);
                        newWorkingHour.setCloseTime(null);
                        newWorkingHour.setClosed(true);
                    }else{
                        newWorkingHour.setOpenTime(workingHourDto.getOpenTime());
                        newWorkingHour.setCloseTime(workingHourDto.getCloseTime());
                        newWorkingHour.setClosed(false);
                    }
                    newWorkingHour.setButchery(updateButchery);
                    workingHoursRepository.save(newWorkingHour);
                }else{
                    if(workingHourDto.isClosed()){
                        existing.setOpenTime(null);
                        existing.setCloseTime(null);
                        existing.setClosed(true);
                    }else{
                        existing.setOpenTime(workingHourDto.getOpenTime());
                        existing.setCloseTime(workingHourDto.getCloseTime());
                        existing.setClosed(false);
                    }
                    workingHoursRepository.save(existing);
                }
            }

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

        butcheryResponse.setWorkingHours(workingHoursRepository.findAllByButcheryId(id));

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

    @Override
    public ButcheryOrderStats getOrderStat(Butchery butchery) {
        int deliveredOrders = orderRepository.getDeliveredOrdersByButchery(butchery);
        int activeOrders = orderRepository.getActiveOrdersByButchery(butchery);
        int newOrders = orderRepository.getNewOrdersByButchery(butchery);


        return new ButcheryOrderStats(activeOrders, newOrders, deliveredOrders);
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
