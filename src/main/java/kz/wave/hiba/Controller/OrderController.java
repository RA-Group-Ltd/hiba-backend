package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.OrderCreateDTO;
import kz.wave.hiba.DTO.OrderReadWithoutUserDTO;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.NotificationCategory;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.NotificationService;
import kz.wave.hiba.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @GetMapping(value = "/getOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orderList = orderService.getAllOrders();
            return ResponseEntity.ok(orderList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/getOrder/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOneOrder(orderId);

            if (order.getId() != null) {
                return ResponseEntity.ok(order);
            } else {
                return new ResponseEntity<>("Order doesn't exist", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/createOrder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        try {
            Order order = orderService.createOrder(orderCreateDTO, request);
//            Optional<Order> order = orderRepository.findById(orderCreateDTO.getId());

            if (order == null) {
                return ResponseEntity.badRequest().build();
            }

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(value = "/updateOrder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateOrder(@RequestBody OrderUpdateDTO orderUpdateDTO, HttpServletRequest request) {
        try {
            Optional<Order> order = orderRepository.findById(orderUpdateDTO.getId());

            orderService.updateOrder(orderUpdateDTO, request);
            return new ResponseEntity<>("Order updated!", HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/updateOrderStatus/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, HttpServletRequest request, @RequestParam("status") OrderStatus newOrderStatus) {

        try {
            orderService.updateOrderStatus(id, request, newOrderStatus);
            notificationService.sendNotificationToUser(id, NotificationCategory.ORDERS);
            return new ResponseEntity<>("Accepted your request and changed status!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("You don't have privilege!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getMyOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByPhone(username);

            List<OrderReadWithoutUserDTO> orderDtos = orderService.getMyOrders(user.getId())
                    .stream()
                    .map(this::transformOrderToDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(orderDtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    private OrderReadWithoutUserDTO transformOrderToDTO(Order order) {
        System.out.println(order.getMenuItems());
        return new OrderReadWithoutUserDTO(order.getId(), order.getOrderStatus(),
                order.getAddress(), order.getButchery(),
                order.isCharity(), order.getMenuItems(), order.getDeliveryDate(),
                order.getTotalPrice(), order.getTotalPrice(), order.getDonation());
    }

    /*@PutMapping(value = "/updateOrderStatus/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_SUPPORT', 'ROLE_BUTCHER', 'ROLE_COURIER')")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderUpdateDTO orderUpdateDTO, HttpServletRequest request) {
        try {
            orderService.updateOrderStatus(orderUpdateDTO, request);
            return new ResponseEntity<>("Accepted your request and changed status!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("You don't have privilege!", HttpStatus.BAD_REQUEST);
        }
    }*/

}
