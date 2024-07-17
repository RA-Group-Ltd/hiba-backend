package kz.wave.hiba.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Config.MailingUtils;
import kz.wave.hiba.DTO.CourierCreateDTO;
import kz.wave.hiba.DTO.CourierUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Response.CourierOrderResponse;
import kz.wave.hiba.Response.CourierOrdersByButcheryResponse;
import kz.wave.hiba.Response.CourierResponse;
import kz.wave.hiba.Response.OrderResponse;
import kz.wave.hiba.Service.CourierService;
import kz.wave.hiba.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the {@link CourierService} interface.
 */
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
    private final ButcheryRepository butcheryRepository;
    private final JwtUtils jwtUtils;
    private final OrderService orderService;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    /**
     * Retrieves all couriers.
     *
     * @return a list of all couriers
     */
    @Override
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    /**
     * Retrieves a courier by its ID.
     *
     * @param id the ID of the courier
     * @return the courier response containing courier details, active and delivered orders count
     */
    @Override
    public CourierResponse getCourier(Long id) {
        Courier courier = courierRepository.findById(id).orElseThrow();
        Long actOrders = orderRepository.countActiveOrdersByCourierId(id);
        Long delOrders = orderRepository.countDeliveredOrdersByCourierId(id);

        return new CourierResponse(courier, delOrders, actOrders);
    }

    /**
     * Retrieves a courier by full name or phone or email.
     *
     * @param userName the name of the user
     * @param userPhone the phone number of the user
     * @return the courier found by full name or phone
     */
    @Override
    public Courier getCourierByFullNameOrPhoneOrEmail(String userName, String userPhone) {
        return courierRepository.findCourierByUserNameOrUserPhone(userName, userPhone);
    }

    /**
     * Creates a new courier.
     *
     * @param courierCreateDTO the data transfer object containing courier creation data
     * @return the created courier
     */
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

    /**
     * Updates an existing courier.
     *
     * @param courierUpdateDTO the data transfer object containing courier update data
     * @return the updated courier, or null if not found
     */
    @Override
    public Courier udpateCourier(CourierUpdateDTO courierUpdateDTO) {
        return null;
    }

    /**
     * Retrieves a courier by user ID.
     *
     * @param userId the ID of the user
     * @return the courier found by user ID
     */
    @Override
    public Courier getCourierByUserId(Long userId) {
        return courierRepository.findByUserId(userId);
    }

    /**
     * Deletes a courier by its ID.
     *
     * @param id the ID of the courier to delete
     */
    @Override
    public void deleteCourierById(Long id) {
        courierRepository.deleteById(id);
    }

    /**
     * Retrieves waiting orders by butchery.
     *
     * @return a list of courier orders by butchery response
     */
    @Override
    public List<CourierOrdersByButcheryResponse> getWaitingOrdersByButchery() {
        List<Butchery> butcheries = butcheryRepository.findAll();
        List<CourierOrdersByButcheryResponse> response = new ArrayList<>();

        for (Butchery butchery : butcheries) {
            int activeOrders = orderRepository.countOrdersByButcheryAndCourierIsNullAndOrderStatus(butchery, OrderStatus.PREPARING_FOR_DELIVERY);

            if (activeOrders == 0) {
                continue;
            }

            CourierOrdersByButcheryResponse resp = new CourierOrdersByButcheryResponse(butchery, activeOrders);
            response.add(resp);
        }
        return response;
    }

    /**
     * Retrieves active orders by butchery ID.
     *
     * @param id the ID of the butchery
     * @param request the HTTP request
     * @return a list of active order responses
     */
    @Override
    public List<OrderResponse> getActiveOrdersByButcheryId(Long id, HttpServletRequest request) {
        User user = jwtUtils.getUserFromRequest(request);
        Courier courier = courierRepository.findByUserId(user.getId());
        List<Order> orderList = orderRepository.findOrdersByCourierAndButcheryId(courier, id);

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orderList) {
            OrderResponse orderResponse = orderService.getOneOrder(order.getId());
            responses.add(orderResponse);
        }

        return responses;
    }

    /**
     * Retrieves waiting orders by butchery ID.
     *
     * @param id the ID of the butchery
     * @return a list of waiting order responses
     */
    @Override
    public List<OrderResponse> getWaitingOrdersByButcheryId(Long id) {
        List<Order> orderList = orderRepository.findOrdersByCourierIsNullAndButcheryIdAndOrderStatus(id, OrderStatus.PREPARING_FOR_DELIVERY);

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orderList) {
            OrderResponse orderResponse = orderService.getOneOrder(order.getId());
            responses.add(orderResponse);
        }

        return responses;
    }

    /**
     * Retrieves active orders by butchery for the courier.
     *
     * @param request the HTTP request
     * @return a list of courier orders by butchery response
     */
    @Override
    public List<CourierOrdersByButcheryResponse> getActiveOrdersByButchery(HttpServletRequest request) {
        User user = jwtUtils.getUserFromRequest(request);
        Courier courier = courierRepository.findByUserId(user.getId());
        List<Butchery> butcheries = butcheryRepository.findAll();
        List<CourierOrdersByButcheryResponse> response = new ArrayList<>();

        for (Butchery butchery : butcheries) {
            int activeOrders = orderRepository.countOrdersByButcheryAndCourier(butchery, courier);

            if (activeOrders == 0) {
                continue;
            }

            CourierOrdersByButcheryResponse resp = new CourierOrdersByButcheryResponse(butchery, activeOrders);
            response.add(resp);
        }
        return response;
    }

    /**
     * Retrieves couriers based on sorting, filtering, and search query.
     *
     * @param sort the sorting order
     * @param filter the filtering criteria
     * @param query the search query
     * @return a list of courier order responses
     */
    @Override
    public List<CourierOrderResponse> getCouriers(String sort, String filter, String query) {
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

        return courierRepository.findCouriers(query, sort, cityList);
    }

    /**
     * Retrieves active orders for the current courier.
     *
     * @param request the HTTP request
     * @return a list of active order responses
     */
    @Override
    public List<OrderResponse> getActiveOrders(HttpServletRequest request) {
        User user = jwtUtils.getUserFromRequest(request);
        Courier courier = courierRepository.findByUserId(user.getId());
        List<Order> orderList = orderRepository.findOrdersByCourier(courier);

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orderList) {
            OrderResponse orderResponse = orderService.getOneOrder(order.getId());
            responses.add(orderResponse);
        }

        return responses;
    }

    /**
     * Retrieves waiting orders for the current courier.
     *
     * @return a list of waiting order responses
     */
    @Override
    public List<OrderResponse> getWaitingOrders() {
        List<Order> orderList = orderRepository.findOrdersByCourierIsNullAndOrderStatus(OrderStatus.PREPARING_FOR_DELIVERY);

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orderList) {
            OrderResponse orderResponse = orderService.getOneOrder(order.getId());
            responses.add(orderResponse);
        }

        return responses;
    }

    @Override
    public Courier verifyCode(Long id, String code, HttpServletRequest request) {
        User user = jwtUtils.getUserFromRequest(request);
        Courier courier = courierRepository.findByUserId(user.getId());

        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            return null;
        }

        Order order = orderOptional.get();

        if (order.getCourier().equals(courier)) {
            ConfirmationCode confirmationCode = confirmationCodeRepository.getConfirmationCodeByUserId(order.getUser().getId());
            if (confirmationCode.getToken().equals(code)) {
                return courier;
            }
        }

        return null;
    }

    /**
     * Generates a random password.
     *
     * @return a random password
     */
    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
