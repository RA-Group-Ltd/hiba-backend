package kz.wave.hiba.Controller;

import kz.wave.hiba.DTO.EmployeeDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Courier;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Enum.StatPeriod;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Response.ButcheryResponse;
import kz.wave.hiba.Response.CourierOrderResponse;
import kz.wave.hiba.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ButcheryService butcheryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private CourierService courierService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> getStatistics(@RequestParam("period") StatPeriod period){
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

    @GetMapping(value = "/butcheries/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> getButcheryById(@PathVariable(value = "id") Long id) {
        System.out.println(id);
        try {
            ButcheryResponse butcheryResponse = butcheryService.getButcheryInfoById(id);

            if (butcheryResponse == null) {
                return new ResponseEntity<>("Not found!", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(butcheryResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("500", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getButcheries")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    public ResponseEntity<?> getButcheries(@RequestParam("sort") String sort, @RequestParam("filter") String filter, @RequestParam("q") String query){
        try{
            List<Butchery> butcheries = butcheryService.getButcheries(sort, filter, query);
            return new ResponseEntity<>(butcheries, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/employees")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO newEmployee) {
        try {
            employeesService.createEmployee(newEmployee);
            return new ResponseEntity<>("Created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/orders")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> getOrders(@RequestParam("period") String period,
                                       @RequestParam("q") String query,
                                       @RequestParam("filter") List<String> filter) {
        try {

            List<Order> orders = orderService.getOrders(query, period, filter);

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/couriers")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_SUPPORT')")
    public ResponseEntity<?> getCouriers(@RequestParam("sort") String sort,
                                         @RequestParam("filter") String filter,
                                         @RequestParam("q") String query){
        try{
            List<CourierOrderResponse> couriers = courierService.getCouriers(sort, filter, query);
            return new ResponseEntity<>(couriers, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
