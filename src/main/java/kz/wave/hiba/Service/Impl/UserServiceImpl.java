package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.DTO.ModelUserDTO;
import kz.wave.hiba.DTO.UserDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.UserRole;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Response.UserResponse;
import kz.wave.hiba.Service.UserFileUploadService;
import kz.wave.hiba.Service.UserRoleService;
import kz.wave.hiba.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Implementation of the {@link UserService} interface.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFileUploadService userFileUploadService;
    private final UserRoleService userRoleService;
    private final JwtUtils jwtUtils;

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user
     */
    @Override
    public User getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    /**
     * Creates a new user.
     *
     * @param userDTO the user data transfer object
     * @return the created user
     */
    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepository.findByPhone(userDTO.getPhone()) != null) {
            return null;
        }

        User newUser = new User();
        newUser.setName(userDTO.getUsername());
        newUser.setPhone(userDTO.getPhone());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setCreatedAt(Instant.now());

        return userRepository.save(newUser);
    }

    /**
     * Logs in a user.
     *
     * @param authDTO the authentication data transfer object
     * @return the user if authentication is successful, null otherwise
     */
    @Override
    public User login(AuthDTO authDTO) {
        User user = userRepository.findByPhone(authDTO.getPhone());
        if (user != null && passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Updates a user.
     *
     * @param userDTO the user data transfer object
     * @param user the user to be updated
     * @return a response entity with the updated user response
     */
    @Override
    public ResponseEntity<?> updateUser(ModelUserDTO userDTO, User user) {
        if (user != null) {
            if (userDTO.getAvatar() != null) {
                user = userFileUploadService.uploadImage(userDTO.getAvatar(), user);
            }

            if (user != null) {
                user.setName(userDTO.getName());
                if (!userDTO.getPhone().isEmpty()) {
                    user.setPhone(userDTO.getPhone());
                }

                userRepository.save(user);

                UserRole userRole = userRoleService.getUserRoleByUserId(user.getId());
                String token = jwtUtils.generateToken(user);

                return new ResponseEntity<>(new UserResponse(token, user, userRole), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to upload image", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Loads a user by their phone number.
     *
     * @param phone the phone number of the user
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }
}
