package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.DTO.AuthCheckDTO;
import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.RoleRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Repository.UserRoleRepository;
import kz.wave.hiba.Repository.VerificationCodeRepository;
import kz.wave.hiba.Response.UserResponse;
import kz.wave.hiba.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Implementation of the {@link AuthService} interface.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleService userRoleService;
    private final UserFileUploadService userFileUploadService;
    private final RoleRepository roleRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * Finds a user by their phone number.
     *
     * @param phoneNumber the phone number of the user
     * @return the user with the specified phone number
     */
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber);
    }

    /**
     * Creates a new user with the specified phone number.
     *
     * @param phoneNumber the phone number of the user
     * @return the created user
     */
    @Override
    public User createUserWithPhoneNumber(String phoneNumber) {
        User newUser = new User();
        newUser.setPhone(phoneNumber);
        return userRepository.save(newUser);
    }

    /**
     * Generates and sends a verification code to the specified user.
     *
     * @param user the user to whom the verification code is to be sent
     */
    @Override
    public void generateAndSendVerificationCode(User user) {
        VerificationCode verificationCode = generateVerificationCode(user);
        verificationCodeRepository.save(verificationCode);
    }

    /**
     * Generates a verification code for the specified user.
     *
     * @param user the user for whom the verification code is to be generated
     * @return the generated verification code
     */
    @Override
    public VerificationCode generateVerificationCode(User user) {
        VerificationCode code = new VerificationCode();
        String verificationCode = String.format("%04d", new Random().nextInt(10000)); // generates a 4-digit code
        code.setToken(verificationCode);
        // Set expirationDate using LocalDateTime
        code.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // code is valid for 10 minutes
        code.setUser(user);
        return code;
    }

    /**
     * Verifies the provided code for the specified phone number.
     *
     * @param phoneNumber the phone number of the user
     * @param code the verification code
     * @return true if the code is valid, false otherwise
     */
    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        // Find user by phone number
        User user = findByPhoneNumber(phoneNumber);
        if (user == null) {
            return false;
        }

        // Find valid verification codes for the user
        List<VerificationCode> validCodes = verificationCodeRepository.findByUserIdAndExpirationDateAfter(user.getId(), LocalDateTime.now());

        // Check if the provided code matches any valid code
        for (VerificationCode validCode : validCodes) {
            if (validCode.getToken().equals(code)) {
                return true;
            }
        }

        verificationCodeRepository.deleteVerificationCodeByUserId(user.getId());

        return false;
    }

    /**
     * Confirms the user with the specified phone number.
     *
     * @param phoneNumber the phone number of the user
     * @return a {@link ResponseEntity} containing the user response and HTTP status
     */
    @Override
    public ResponseEntity<?> confirmUser(String phoneNumber) {
        User user = findByPhoneNumber(phoneNumber);
        if (user != null) {
            verificationCodeRepository.deleteVerificationCodeByUserId(user.getId());
            if (!user.isConfirmed()) {
                userRepository.save(user);
                return new ResponseEntity<>("", HttpStatus.OK);
            } else {
                UserRole userRole = userRoleService.getUserRoleByUserId(user.getId());
                String token = jwtUtils.generateToken(user);
                return new ResponseEntity<>(new UserResponse(token, user, userRole), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    /**
     * Completes the registration of the user with the specified phone number.
     *
     * @param phoneNumber the phone number of the user
     * @param name the name of the user
     * @param photo the profile photo of the user
     * @return the updated user
     */
    @Override
    public User completeRegistration(String phoneNumber, String name, MultipartFile photo) {
        // Find user by phone number
        User user = userRepository.findByPhone(phoneNumber);
        if (user != null) {
            user.setName(name);
            user.setCreatedAt(Instant.now());
            user.setConfirmed(true);

            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userFileUploadService.uploadImage(photo, user);
            Role role = roleRepository.findByName("ROLE_USER");
            UserRoleId userRoleId = new UserRoleId(user.getId(), role.getId());
            UserRole userRole = new UserRole(userRoleId, user, role);
            userRoleRepository.save(userRole);

            userRepository.save(user);
            return user;
        }
        return null;
    }

    /**
     * Checks if the provided token is valid for the specified username.
     *
     * @param token the token to be checked
     * @param username the username to be checked
     * @return true if the token is valid for the username, false otherwise
     */
    public boolean isValidTokenForUser(String token, String username) {
        // Get user from token
        User userFromToken = jwtUtils.validateAndGetUserFromToken(token);

        // Get user from database
        User userFromDB = userRepository.findByPhone(username);

        // Check if users and tokens match
        return userFromToken != null && userFromDB != null && userFromToken.getId().equals(userFromDB.getId());
    }

    /**
     * Finds all connected users.
     *
     * @return a list of connected users
     */
    @Override
    public List<User> findConnectedUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by phone number.
     *
     * @param phone the phone number of the user
     * @return the user with the specified phone number
     */
    @Override
    public User findIdByPhoneNumber(String phone) {
        return null;
    }

    /**
     * Counts the total number of users.
     *
     * @return the total number of users
     */
    @Override
    public long quantityOfUsers() {
        return userRepository.countUsers();
    }

    /**
     * Gets the user ID by phone number.
     *
     * @param phone the phone number of the user
     * @return an {@link Optional} containing the user ID if found, or empty if not found
     */
    @Override
    public Optional<Long> getUserIdByPhone(String phone) {
        return userRepository.findIdByPhone(phone);
    }

    /**
     * Checks if the phone number exists in the database.
     *
     * @param authCheckDTO the authentication check data transfer object
     * @return true if the phone number exists, false otherwise
     */
    @Override
    public boolean checkPhone(AuthCheckDTO authCheckDTO) {
        return userRepository.findByPhone(authCheckDTO.getPhone()) != null;
    }

    /**
     * Registers a new user with the provided authentication data.
     *
     * @param authDTO the authentication data transfer object
     * @return the registered user, or null if the phone number already exists
     */
    @Override
    public User registerUser(AuthDTO authDTO) {
        if (userRepository.findByPhone(authDTO.getPhone()) != null) {
            return null;
        }

        User newUser = new User();
        newUser.setPhone(authDTO.getPhone());
        newUser.setCreatedAt(Instant.now());
        newUser.setPassword(passwordEncoder.encode(authDTO.getPassword()));

        userRepository.save(newUser);

        Role role = roleRepository.findByName("ROLE_USER");
        UserRoleId userRoleId = new UserRoleId(newUser.getId(), role.getId());
        UserRole userRole = new UserRole(userRoleId, newUser, role);
        userRoleRepository.save(userRole);

        return newUser;
    }

    /**
     * Logs in a user with the provided authentication data.
     *
     * @param authDTO the authentication data transfer object
     * @return the user if authentication is successful, or null if authentication fails
     */
    @Override
    public User login(AuthDTO authDTO) {
        User user = userRepository.findByUsername(authDTO.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
                return user;
            }
        }

        return null;
    }
}
