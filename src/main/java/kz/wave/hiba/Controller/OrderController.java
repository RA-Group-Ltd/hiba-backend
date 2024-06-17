package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.CancelOrderDTO;
import kz.wave.hiba.DTO.OrderCreateDTO;
import kz.wave.hiba.DTO.OrderReadWithoutUserDTO;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.NotificationCategory;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Response.OrderResponse;
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
    private final ButcheryRepository butcheryRepository;

    @GetMapping(value = "/")
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

    @GetMapping(value = "/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneOrder(@PathVariable Long orderId) {
        try {
            OrderResponse order = orderService.getOneOrder(orderId);

            if (order.getOrder() != null && order.getOrder().getId() != null) {
                return ResponseEntity.ok(order);
            } else {
                return new ResponseEntity<>("Order doesn't exist", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        try {
            Order order = orderService.createOrder(orderCreateDTO, request);

            if (order == null) {
                return ResponseEntity.badRequest().build();
            }

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_BUTCHER')")
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


    @PutMapping(value = "/cancel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER','ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long id, @RequestBody CancelOrderDTO cancelOrderDTO) {
        try {
            orderService.cancelOrder(id);
            return new ResponseEntity<>("Order Status changed to CANCEL", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getMyOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByPhone(username);

            List<OrderResponse> orderResponses = orderService.getMyOrders(user.getId());

            return new ResponseEntity<>(orderResponses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getMyActiveOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyActiveOrders(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByPhone(username);

            List<OrderReadWithoutUserDTO> orderDtos = orderService.getMyActiveOrders(user.getId())
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
                order.getTotalPrice(), order.getDeliveryPrice(), order.getDonation(), order.getPackages());
    }

    @GetMapping(value = "/getButcheryOrders/{status}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_BUTCHER')")
    public ResponseEntity<?> getButcheryOrders(@PathVariable("status") String status, HttpServletRequest request){
        try {
            String token =  jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);
            Butchery butchery = butcheryRepository.findButcheryByOwner(user);

            OrderStatus orderStatus = status.equals("new") ? OrderStatus.AWAITING_CONFIRMATION : (status.equals("archive") ? OrderStatus.DELIVERED : null);

            List<Order> orders = orderRepository.findOrdersByButcheryAndOrderStatus(butchery, orderStatus);

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }


}
