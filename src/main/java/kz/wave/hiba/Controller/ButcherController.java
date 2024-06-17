package kz.wave.hiba.Controller;

import com.google.api.Http;
import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.ButcherEmployeeCreateDTO;
import kz.wave.hiba.Entities.Butcher;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.ButcherRepository;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ButcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/butcher")
public class ButcherController {

    @Autowired
    private ButcherRepository butcherRepository;

    @Autowired
    private ButcherService butcherService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ButcheryRepository butcheryRepository;

    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getByButcherId(@PathVariable Long id) {
        return butcherRepository.findById(id)
                .map(butchery -> ResponseEntity.ok().body(butchery))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER')")
    public ResponseEntity<?> addButcherEmployee(@RequestBody ButcherEmployeeCreateDTO butcheryEmployeeCreateDTO, HttpServletRequest request) {
        try {
            String token =  jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(currentUser);

            Butcher butcher = butcherService.addButcherEmployee(butcheryEmployeeCreateDTO, user);

            if (butcher == null) {
                return new ResponseEntity<>("User exist!", HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(butcher, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Butchery employee already exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_BUTCHER')")
    public ResponseEntity<?> deleteButcheryEmployeeById(@PathVariable Long id,HttpServletRequest request){
        try{
            String token =  jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(token);
            User butcheryOwner = userRepository.findByUsername(currentUser);

            butcherService.deleteButcherByUserId(id,butcheryOwner);

            return new ResponseEntity<>("Butchery employee deleted successfully!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
