package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.CourierCreateDTO;
import kz.wave.hiba.Entities.Courier;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Response.CourierOrdersByButcheryResponse;
import kz.wave.hiba.Response.CourierResponse;
import kz.wave.hiba.Response.OrderResponse;
import kz.wave.hiba.Service.AuthService;
import kz.wave.hiba.Service.CourierService;
import kz.wave.hiba.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import java.util.List;

@RestController
@RequestMapping(value = "/courier")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final OrderService orderService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllCouriers() {
        try {
            List<Courier> couriers = courierService.getAllCouriers();
            return new ResponseEntity<>(couriers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_SUPPORT')")
    public ResponseEntity<?> getCourier(@PathVariable Long id) {
        try {
            CourierResponse courier = courierService.getCourier(id);
            return new ResponseEntity<>(courier, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> createCourier(@RequestBody CourierCreateDTO courierCreateDTO) {
        try {
            courierService.createCourier(courierCreateDTO);
            return new ResponseEntity<>("Created new courier", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getCourierOrders/{courierId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_COURIER')")
    public ResponseEntity<?> getOrdersByCourier(HttpServletRequest request,
                                                @PathVariable(name = "courierId") Long courierId,
                                                @RequestParam(name = "filter") List<String> filter,
                                                @RequestParam(name = "startDate", required = false) Long startDate,
                                                @RequestParam(name = "endDate", required = false) Long endDate) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByPhone(currentUser);

            // Получаем курьера из текущего пользователя
            if (user.getRoles().contains("ROLE_COURIER")) {
                Courier courier = courierService.getCourierByUserId(user.getId());
                if (courier == null || !courier.getId().equals(courierId)) {
                    return new ResponseEntity<>("You are not allowed to view other couriers' orders", HttpStatus.FORBIDDEN);
                }
            }


            List<Order> orders = orderService.getOrdersByCourier(courierId, filter, startDate, endDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getDeliveredOrdersByCourier/{courierId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_COURIER')")
    public ResponseEntity<?> getActiveOrdersByCourier(@PathVariable Long courierId, HttpServletRequest request) {
        try {
             String token = jwtUtils.getTokenFromRequest(request);
             String currentUser = jwtUtils.getUsernameFromToken(token);
             User user = userRepository.findByPhone(currentUser);

             if (user.getRoles().contains("ROLE_COURIER")) {
                 Courier courier = courierService.getCourierByUserId(user.getId());
                 if (courier == null || !courier.getId().equals(courierId)) {
                     return new ResponseEntity<>("You are not allowed to view other couriers' orders", HttpStatus.FORBIDDEN);
                 }
             }

            long activeOrders = orderService.getDeliveredOrdersByCourierId(courierId);
            return new ResponseEntity<>(activeOrders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable Long id) {
        try {
            courierService.deleteCourierById(id);
            return new ResponseEntity<>("Courier deleted successfully!", HttpStatus.OK);
        } catch (NullPointerException n) {
            n.printStackTrace();
            return new ResponseEntity<>("Courier doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/activeOrders/byButchery")
    public ResponseEntity<?> getActiveOrdersByButchery(HttpServletRequest request) {
        try {
            List<CourierOrdersByButcheryResponse> courierOrdersByButcheryResponses = courierService.getActiveOrdersByButchery(request);
            return new ResponseEntity<>(courierOrdersByButcheryResponses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/waitingOrders/byButchery")
    public ResponseEntity<?> getWaitingOrdersByButchery() {
        try {
            List<CourierOrdersByButcheryResponse> courierOrdersByButcheryResponses = courierService.getWaitingOrdersByButchery();
            return new ResponseEntity<>(courierOrdersByButcheryResponses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/activeOrders/byButchery/{id}")
    public ResponseEntity<?> getActiveOrdersByButchery(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        try {
            List<OrderResponse> orderResponses = courierService.getActiveOrdersByButcheryId(id, request);
            return new ResponseEntity<>(orderResponses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/waitingOrders/byButchery/{id}")
    public ResponseEntity<?> getWaitingOrdersByButchery(@PathVariable(name = "id") Long id) {
        try {
            List<OrderResponse> orderResponses = courierService.getWaitingOrdersByButcheryId(id);
            return new ResponseEntity<>(orderResponses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/activeOrders")
    @PreAuthorize("hasAnyRole('ROLE_COURIER')")
    public ResponseEntity<?> getActiveOrders(HttpServletRequest request) {
        try {
            List<OrderResponse> orderList = courierService.getActiveOrders(request);
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/waitingOrders")
    @PreAuthorize("hasAnyRole('ROLE_COURIER')")
    public ResponseEntity<?> getWaitingOrders() {
        try {
            List<OrderResponse> orderList = courierService.getWaitingOrders();
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "/confirmationCode/{id}")
    @PreAuthorize("hasAnyRole('ROLE_COURIER')")
    public ResponseEntity<?> verifyCode(@PathVariable(name = "id") Long id, @RequestParam String code, HttpServletRequest request) {
        try {
            courierService.verifyCode(id, code, request);
            return new ResponseEntity<>("Verified code matched successfully!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Verify code doesn't match!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
