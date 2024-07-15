package kz.wave.hiba.Controller;

import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.ModelUserDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.UserFileUploadService;
import kz.wave.hiba.Service.UserRoleService;
import kz.wave.hiba.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserFileUploadService userFileUploadService;

    @GetMapping(value = "/{id}")
    public User getOneUser(@PathVariable Long id) {
        return userService.getOneUser(id);
    }

    @GetMapping(value = "/")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping(value = "/updateUser")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadImage(@ModelAttribute ModelUserDTO userDTO, HttpServletRequest request) {
        try {
            String userToken = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(userToken);
            User user = userRepository.findByUsername(username);


            return userService.updateUser(userDTO, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/avatar/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] viewPic(@PathVariable(name = "id") Long id) throws IOException {
        return userFileUploadService.getImage(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
