package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Entities.Role;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.UserRole;
import kz.wave.hiba.Repository.ChatRepository;
import kz.wave.hiba.Repository.RoleRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Repository.UserRoleRepository;
import kz.wave.hiba.Response.SupportChatResponse;
import kz.wave.hiba.Response.EmployeeResponse;
import kz.wave.hiba.Service.ChatService;
import kz.wave.hiba.Service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/employees")
public class EmployeesController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatService chatService;
    @Autowired
    private RoleRepository roleRepository;
    @GetMapping(value = "/")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<?> getEmployees(@RequestParam("sort") String sort, @RequestParam("filter") List<String> filter, @RequestParam("q") String query){
        try{
            if(filter == null || filter.size() == 0)
                filter = Arrays.asList("ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_SUPPORT");
            List<User> employees = employeesService.getEmployees(sort, filter, query);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_SUPPORT', 'ROLE_ADMIN')")
    public ResponseEntity<?> getEmployee(@PathVariable(name = "id") Long id) {
        try {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            User employee = employeesService.getEmployee(id);
            employeeResponse.setUser(employee);
            if (employee.getRoles().contains("ROLE_SUPPORT")) {
                employeeResponse.setChats(chatRepository.countAllBySupportId(id));
            }
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        /*if ROLE_SUPPORT
                chat_count with status closed or archive*/
    }

    @GetMapping(value = "/chats/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_SUPPORT', 'ROLE_ADMIN')")
    public ResponseEntity<?> getEmployeeChats(@PathVariable(name = "id") Long id,
                                              @RequestParam(name = "filter") List<String> filter,
                                              @RequestParam(name = "startDate", required = false) Long startDate,
                                              @RequestParam(name = "endDate", required = false) Long endDate,
                                              HttpServletRequest request){
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);

            String userRole = userRoleRepository.getByUserId(user.getId()).getRole().getName();
            String employeeRole = userRoleRepository.getByUserId(id).getRole().getName();

            if (employeeRole.equals("ROLE_SUPPORT") && (!userRole.equals("ROLE_SUPPORT") || Objects.equals(user.getId(), id))) {
                return new ResponseEntity<>(chatService.filterChatsBySupportId(id, filter, startDate, endDate), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }
}
