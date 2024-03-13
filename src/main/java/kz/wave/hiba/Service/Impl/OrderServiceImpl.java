package kz.wave.hiba.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.DTO.OrderCreateDTO;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ButcherRepository butcherRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOneOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Override
    public Order createOrder(OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByPhone(currentUser);

        Address address = addressRepository.findAddressByUserId(user.getId());

        Butcher butcher = butcherRepository.findByUserId(user.getId());

        if (orderRepository.findByButcher(orderCreateDTO.getButcher()) != null) {
            return null;
        }

        Order order = new Order();
        order.setOrderStatus(OrderStatus.IN_PROCESS);
        order.setUser(user);
        order.setAddress(address);
        order.setButcher(butcher);

        return orderRepository.save(order);
    }

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

    public Order updateOrderStatus(OrderUpdateDTO orderUpdateDTO, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByPhone(currentUser);
        Order order = orderRepository.findById(orderUpdateDTO.getId()).orElseThrow();

        order.setOrderStatus(orderUpdateDTO.getOrderStatus());

        return orderRepository.save(order);
    }

    /*@Override
    public Order updateOrderStatus(OrderUpdateDTO orderUpdateDTO, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByPhone(currentUser);
        Order order = orderRepository.findById(orderUpdateDTO.getId()).orElseThrow();
        UserRole userRole = userRoleRepository.getByUserId(user.getId());

        if (userRole.getRole().getName().equals("ROLE_SUPERADMIN")) {
            order.setOrderStatus(OrderStatus.AWAITING_CONFIRMATION);
        } else if (userRole.getRole().getName().equals("ROLE_ADMIN")) {
            order.setOrderStatus(OrderStatus.PREPARING_FOR_DELIVERY);
        } else if (userRole.getRole().getName().equals("ROLE_SUPPORT")) {
            order.setOrderStatus(OrderStatus.ON_THE_WAY);
        } else if (userRole.getRole().getName().equals("ROLE_COURIER")) {
            order.setOrderStatus(OrderStatus.DELIVERED);
        } else {
            order.setOrderStatus(OrderStatus.DELIVERY_TOMORROW);
        }

        return orderRepository.save(order);
    }*/

}