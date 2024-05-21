package kz.wave.hiba.Controller;

import kz.wave.hiba.Enum.StatPeriod;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Service.AuthService;
import kz.wave.hiba.Service.ButcheryService;
import kz.wave.hiba.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(value = "/statistics")
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ButcheryService butcheryService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/{period}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> getStatistics(@PathVariable StatPeriod period){
        try {
            Instant startDate = Instant.now();
            switch (period) {
                case MONTH:
                    startDate = startDate.minusSeconds(60 * 60 * 24 * 30);
                    break;
                case ALL:
                default:
                case HALFYEAR:
                    startDate = startDate.minusSeconds(60 * 60 * 24 * 183);
                case YEAR:
                    startDate = startDate.minusSeconds(60 * 60 * 24 * 365);
            }
            int donations = orderRepository.getDonationsByPeriod(startDate);

            return new ResponseEntity<>(donations, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantityOfButcheries")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> quantityOfButcheries() {
        try {
            Long countButchery = butcheryService.quantityOfButcheries();
            return new ResponseEntity<>(countButchery, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantityOfUsers")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> quantityOfUsers() {
        try {
            Long countOfUsers = authService.quantityOfUsers();
            return new ResponseEntity<>(countOfUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantityOfOrders")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> quantityOfOrders() {
        try {
            Long countOfOrders = orderService.quantityOfOrders();
            return new ResponseEntity<>(countOfOrders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }
}
