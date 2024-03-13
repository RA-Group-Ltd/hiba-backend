package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.ReviewCreateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ButcheryRepository butcheryRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/{butcheryId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> createReview(@RequestBody ReviewCreateDTO reviewCreateDTO, @PathVariable Long butcheryId, HttpServletRequest request) {
        try {
            String userToken = jwtUtils.getTokenFromRequest(request);
            String currentUser = jwtUtils.getUsernameFromToken(userToken);
            User userPhone = userRepository.findByPhone(currentUser);
            System.out.println(currentUser);
            if (userPhone == null) {
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
            }

            Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryId);
            if (butcheryOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            reviewService.createReview(reviewCreateDTO, currentUser, butcheryId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
