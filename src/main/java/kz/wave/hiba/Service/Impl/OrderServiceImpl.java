package kz.wave.hiba.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.DTO.OrderCreateDTO;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Enum.StatPeriod;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Response.OrderMenuResponse;
import kz.wave.hiba.Response.OrderResponse;
import kz.wave.hiba.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Implementation of the {@link OrderService} interface.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ButcheryRepository butcheryRepository;
    private final MenuRepository menuRepository;
    private final ButcheryCategoryRepository butcheryCategoryRepository;
    private final CourierRepository courierRepository;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    /**
     * Retrieves all orders.
     *
     * @return a list of all orders
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieves an order by its ID and constructs a response with detailed menu items.
     *
     * @param id the ID of the order
     * @return the order response with detailed menu items
     */
    @Override
    public OrderResponse getOneOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        List<OrderMenuResponse> menuList = new ArrayList<>();
        Long menuId;

        for (Map.Entry<Long, Integer> el : order.getMenuItems().entrySet()) {
            menuId = el.getKey();
            Optional<Menu> menuOptional = menuRepository.findById(menuId);
            Menu menu = menuOptional.get();

            OrderMenuResponse menuResponse = new OrderMenuResponse();
            menuResponse.setId(menu.getId());
            menuResponse.setName(menu.getName());
            menuResponse.setDescription(menu.getDescription());
            menuResponse.setWeight(menu.getWeight());
            menuResponse.setIsWholeAnimal(menu.getIsWholeAnimal());
            menuResponse.setPrice(menu.getPrice());
            menuResponse.setImage(menu.getImage());
            menuResponse.setQuantity(el.getValue());

            Optional<ButcheryCategory> butcheryCategoryOptional = butcheryCategoryRepository.findById(menu.getButcheryCategoryId());
            if (butcheryCategoryOptional.isPresent()) {
                ButcheryCategory butcheryCategory = butcheryCategoryOptional.get();
                menuResponse.setButcheryCategory(butcheryCategory);
            }

            menuList.add(menuResponse);
        }

        return new OrderResponse(order, menuList);
    }

    /**
     * Creates a new order.
     *
     * @param orderCreateDTO the data transfer object containing order creation data
     * @param request the HTTP request
     * @return the created order, or null if the butchery is not found
     */
    @Override
    public Order createOrder(OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByPhone(currentUser);

        Optional<Butchery> butcheryOptional = butcheryRepository.findById(orderCreateDTO.getButchery().getId());

        if (butcheryOptional.isEmpty()) {
            return null;
        }

        Map<Long, Integer> menuItemMap = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : orderCreateDTO.getMenuItemsId().entrySet()) {
            Long menuId = entry.getKey();
            Integer count = entry.getValue();

            Menu menuItem = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

            menuItemMap.put(menuItem.getId(), count);
        }

        Butchery butchery = butcheryOptional.get();

        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_CONFIRMATION);
        order.setUser(user);
        if (orderCreateDTO.isCharity()) {
            order.setAddress(null);
        } else {
            Optional<Address> addressOptional = addressRepository.findById(orderCreateDTO.getAddress().getId());
            Address address = addressOptional.get();
            order.setAddress(address);
        }
        order.setButchery(butchery);
        order.setMenuItems(menuItemMap);
        order.setCharity(orderCreateDTO.isCharity());
        order.setDeliveryDate(Instant.ofEpochMilli(orderCreateDTO.getDeliveryDate()));
        order.setCreatedAt(Instant.now().atZone(ZoneId.of("UTC")).toInstant());
        order.setTotalPrice(orderCreateDTO.getTotalPrice());
        order.setDeliveryPrice(orderCreateDTO.getDeliveryPrice());
        order.setDonation(orderCreateDTO.getDonation());
        order.setSender(orderCreateDTO.getSender());
        order.setPackages(orderCreateDTO.getPackages());

        return orderRepository.save(order);
    }

    /**
     * Updates an existing order.
     *
     * @param orderUpdateDTO the data transfer object containing order update data
     * @param request the HTTP request
     * @return the updated order, or null if the order is not found
     */
    @Override
    public Order updateOrder(OrderUpdateDTO orderUpdateDTO, HttpServletRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(orderUpdateDTO.getId());

        if (orderOptional.isEmpty()) {
            return null;
        }

        Order order = orderOptional.get();

        if (order.getUser() == orderUpdateDTO.getAddress().getUser()) {
            order.setOrderStatus(OrderStatus.AWAITING_CONFIRMATION);
            order.setAddress(orderUpdateDTO.getAddress());
        }

        return orderRepository.save(order);
    }

    /**
     * Updates the status of an existing order.
     *
     * @param id the ID of the order
     * @param request the HTTP request
     * @param newOrderStatus the new order status
     * @return the updated order
     */
    public Order updateOrderStatus(Long id, HttpServletRequest request, OrderStatus newOrderStatus) {
        User user = jwtUtils.getUserFromRequest(request);
        Courier courier = courierRepository.findByUserId(user.getId());

        Order order = orderRepository.findById(id).orElseThrow();

        if (courier != null) {
            if (order.getCourier() == null && order.getOrderStatus().equals(OrderStatus.PREPARING_FOR_DELIVERY)) {
                order.setCourier(courier);
            } else if (Objects.equals(order.getCourier().getId(), courier.getId())) {
                if (newOrderStatus.equals(OrderStatus.DELIVERED)) {
                    ConfirmationCode code = new ConfirmationCode();
                    String verificationCode = String.format("%04d", new Random().nextInt(10000)); // generates a 4-digit code
                    code.setToken(verificationCode);
                    // Set expirationDate using LocalDateTime
                    code.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // code is valid for 10 minutes
                    code.setUser(order.getUser());
                    code.setOrder(order);
                    confirmationCodeRepository.save(code);
                    return order;
                }
            }
        }

        order.setOrderStatus(newOrderStatus);

        return orderRepository.save(order);
    }

    /**
     * Retrieves all orders for a specific user.
     *
     * @param id the ID of the user
     * @return a list of order responses for the user
     */
    @Override
    public List<OrderResponse> getMyOrders(Long id) {
        List<Order> orders = orderRepository.findOrdersByUserIdSortedNatural(id);

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = getOneOrder(order.getId());
            orderResponse.getOrder().setUser(null);
            responses.add(orderResponse);
        }

        return responses;
    }

    /**
     * Retrieves all active orders for a specific user.
     *
     * @param id the ID of the user
     * @return a list of active orders for the user
     */
    @Override
    public List<Order> getMyActiveOrders(Long id) {
        return orderRepository.findOrdersByUserIdSortedActive(id);
    }

    /**
     * Retrieves the total quantity of orders.
     *
     * @return the total quantity of orders
     */
    @Override
    public long quantityOfOrders() {
        return orderRepository.countOrders();
    }

    /**
     * Retrieves orders for a specific courier based on filters and date range.
     *
     * @param courierId the ID of the courier
     * @param filter a list of filters
     * @param startDate the start date for the orders
     * @param endDate the end date for the orders
     * @return a list of orders for the courier
     */
    @Override
    public List<Order> getOrdersByCourier(Long courierId, List<String> filter, Long startDate, Long endDate) {
        Instant stDate = null;
        Instant edDate = null;

        try {
            if (startDate != null) {
                stDate = Instant.ofEpochMilli(startDate);
            }
            if (endDate != null) {
                edDate = Instant.ofEpochMilli(endDate);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        return orderRepository.getCourierOrders(courierId, filter, stDate, edDate);
    }

    /**
     * Retrieves the total number of delivered orders for a specific courier.
     *
     * @param courierId the ID of the courier
     * @return the total number of delivered orders for the courier
     */
    @Override
    public long getDeliveredOrdersByCourierId(Long courierId) {
        return orderRepository.countDeliveredOrdersByCourierId(courierId);
    }

    /**
     * Retrieves orders based on query, period, and statuses.
     *
     * @param query the search query
     * @param period the period for the orders
     * @param statuses a list of order statuses
     * @return a list of orders matching the criteria
     */
    @Override
    public List<Order> getOrders(String query, String period, List<String> statuses) {
        Instant startDate = Instant.now();
        startDate = switch (StatPeriod.valueOf(period)) {
            default -> startDate.minusSeconds(60 * 60 * 24);
            case WEEK -> startDate.minusSeconds(60 * 60 * 24 * 7);
            case MONTH -> startDate.minusSeconds(60 * 60 * 24 * 30);
            case QUARTER -> startDate.minusSeconds(60 * 60 * 24 * 91);
            case HALFYEAR -> startDate.minusSeconds(60 * 60 * 24 * 183);
            case YEAR -> startDate.minusSeconds(60 * 60 * 24 * 365);
            case ALL -> null;
        };
        return orderRepository.findOrders(query, startDate, statuses);
    }

    /**
     * Cancels an order.
     *
     * @param id the ID of the order to cancel
     * @return the updated order, or null if the order is not found or cannot be canceled
     */
    @Override
    public Order cancelOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            return null;
        }

        Order updateOrder = orderOptional.get();

        if (OrderStatus.AWAITING_CONFIRMATION.equals(updateOrder.getOrderStatus())) {
            updateOrder.setOrderStatus(OrderStatus.CANCEL);
            orderRepository.save(updateOrder);
        }

        return updateOrder;
    }
}
