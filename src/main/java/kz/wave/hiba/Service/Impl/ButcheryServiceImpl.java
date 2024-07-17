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

import java.time.Instant;
import java.util.*;

/**
 * Implementation of the {@link ButcheryService} interface.
 */
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

    /**
     * Retrieves all butcheries.
     *
     * @return a list of all butcheries
     */
    @Override
    public List<Butchery> getAllButchery() {
        return butcheryRepository.findAll();
    }

    /**
     * Retrieves a butchery by its ID and returns its response representation.
     *
     * @param id the ID of the butchery
     * @return the response representation of the butchery
     */
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

    /**
     * Creates a new butchery.
     *
     * @param butcheryCreateDTO the data transfer object containing butchery creation data
     * @param city the city in which the butchery is located
     * @return the created butchery
     */
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

        for (DayOfWeek day : DayOfWeek.values()) {
            WorkingHours workingHours = new WorkingHours();
            workingHours.setButchery(butchery);
            workingHours.setDayOfWeek(day);
            if (day == DayOfWeek.SUNDAY) {
                workingHours.setClosed(true);
            } else {
                workingHours.setOpenTime("00:00");
                workingHours.setCloseTime("00:00");
                workingHours.setClosed(false);
            }
            workingHoursRepository.save(workingHours);
        }

        return butchery;
    }

    /**
     * Updates an existing butchery.
     *
     * @param butcheryUpdateDTO the data transfer object containing butchery update data
     * @param city the city in which the butchery is located
     * @return the updated butchery, or null if not found
     */
    @Override
    public Butchery updateButchery(ButcheryUpdateDTO butcheryUpdateDTO, City city) {
        Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryUpdateDTO.getId());

        if (butcheryOptional.isPresent()) {
            Butchery updateButchery = butcheryOptional.get();

            updateButchery.setAddress(butcheryUpdateDTO.getAddress());
            updateButchery.setCity(city);
            updateButchery.setPhone(butcheryUpdateDTO.getPhone());

            updateButchery = butcheryFileUploadService.uploadImage(butcheryUpdateDTO.getImage(), updateButchery);

            for (WorkingHourDTO workingHourDto : butcheryUpdateDTO.getWorkingHours()) {
                WorkingHours existing = workingHoursRepository.findWorkingHoursByButcheryIdAndDayOfWeek(butcheryUpdateDTO.getId(), workingHourDto.getDayOfWeek());
                if (existing == null) {
                    WorkingHours newWorkingHour = new WorkingHours();
                    newWorkingHour.setClosed(workingHourDto.isClosed());
                    newWorkingHour.setDayOfWeek(workingHourDto.getDayOfWeek());

                    if (workingHourDto.isClosed()) {
                        newWorkingHour.setOpenTime(null);
                        newWorkingHour.setCloseTime(null);
                        newWorkingHour.setClosed(true);
                    } else {
                        newWorkingHour.setOpenTime(workingHourDto.getOpenTime());
                        newWorkingHour.setCloseTime(workingHourDto.getCloseTime());
                        newWorkingHour.setClosed(false);
                    }
                    newWorkingHour.setButchery(updateButchery);
                    workingHoursRepository.save(newWorkingHour);
                } else {
                    if (workingHourDto.isClosed()) {
                        existing.setOpenTime(null);
                        existing.setCloseTime(null);
                        existing.setClosed(true);
                    } else {
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

    /**
     * Deletes a butchery by its ID.
     *
     * @param id the ID of the butchery to be deleted
     */
    @Override
    public void deleteButchery(Long id) {
        butcheryRepository.deleteById(id);
    }

    /**
     * Counts the total number of butcheries.
     *
     * @return the total number of butcheries
     */
    @Override
    public Long quantityOfButcheries() {
        return butcheryRepository.countButcheries();
    }

    /**
     * Retrieves detailed information about a butchery by its ID.
     *
     * @param id the ID of the butchery
     * @return the response representation of the butchery
     */
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

    /**
     * Retrieves a list of butcheries based on the specified sorting, filtering, and search query.
     *
     * @param sort the sorting order
     * @param filter the filtering criteria
     * @param query the search query
     * @return a list of butcheries matching the specified criteria
     */
    @Override
    public List<Butchery> getButcheries(String sort, String filter, String query) {
        List<Long> cityList = new ArrayList<>();
        if (!filter.isEmpty()) {
            String[] country_cities = filter.split(";");
            for (String country_city : country_cities) {
                String[] country_and_cities = country_city.split(":");
                if (country_and_cities.length > 1) {
                    String countryName = country_and_cities[0];
                    String[] cityNames = country_and_cities[1].split(",");
                    if (cityNames.length > 0) {
                        Country country = countryRepository.findByName(countryName);
                        List<Long> cities = cityRepository.findAllIdsByCountryAndNameInList(country, cityNames);
                        cityList.addAll(cities);
                    }
                }
            }
        }
        if (cityList.isEmpty()) {
            cityList = null;
        }
        return butcheryRepository.findButcheries(sort, query, cityList);
    }

    /**
     * Retrieves order statistics for a butchery.
     *
     * @param butchery the butchery entity
     * @return the order statistics for the butchery
     */
    @Override
    public ButcheryOrderStats getOrderStat(Butchery butchery) {
        int deliveredOrders = orderRepository.getDeliveredOrdersByButchery(butchery);
        int activeOrders = orderRepository.getActiveOrdersByButchery(butchery);
        int newOrders = orderRepository.getNewOrdersByButchery(butchery);

        return new ButcheryOrderStats(activeOrders, newOrders, deliveredOrders);
    }

    /**
     * Generates a random password.
     *
     * @return the generated password
     */
    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
