package kz.wave.hiba.Controller;

import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Config.TelegramBot;
import kz.wave.hiba.Response.UserResponse;
import kz.wave.hiba.DTO.AuthCheckDTO;
import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.DTO.CompleteRegistrationDTO;
import kz.wave.hiba.DTO.VerificationDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.UserRole;
import kz.wave.hiba.Service.AuthService;
import kz.wave.hiba.Service.UserRoleService;
import kz.wave.hiba.Service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private TelegramBot telegramBot;

    @PostMapping("/check")
    public ResponseEntity<?> checkPhone(@RequestBody AuthCheckDTO authCheckDTO) {
        boolean exists = authService.checkPhone(authCheckDTO);
        if (exists) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/sendOrVerifyCode")
    public ResponseEntity<?> sendOrVerifyCode(@RequestBody VerificationDTO verificationDTO) {
        // Проверяем, есть ли пользователь с таким номером телефона
//        User user = authService.findByPhoneNumber(verificationDTO.getPhoneNumber());
//        if (user == null) {
//            // Если пользователя нет, создаем нового и отправляем код
//            user = authService.createUserWithPhoneNumber(verificationDTO.getPhoneNumber());
//        }

        // Генерируем и отправляем код, не зависимо от того, новый пользователь или нет
//        telegramBot.generateAndSendVerificationCode(user);

        String deepLink = "https://t.me/" + telegramBot.getBotUsername()+ "?start="+ verificationDTO.getPhoneNumber();
        return ResponseEntity.ok(deepLink);
    }

    @PostMapping("/confirmCode")
    public ResponseEntity<?> confirmCode(@RequestBody VerificationDTO verificationDTO) {
        // Проверяем код
        boolean isCodeValid = authService.verifyCode(verificationDTO.getPhoneNumber(), verificationDTO.getCode());
        if (isCodeValid) {
            // Если код правильный, подтверждаем пользователя
            return authService.confirmUser(verificationDTO.getPhoneNumber());
        } else {
            return ResponseEntity.badRequest().body("Неверный код подтверждения или срок его действия истек.");
        }
    }

    @PostMapping("/completeRegistration")
    public ResponseEntity<?> completeRegistration(@ModelAttribute CompleteRegistrationDTO registrationDTO) {
        // Завершаем регистрацию пользователя
        User user = authService.completeRegistration(registrationDTO.getPhoneNumber(), registrationDTO.getName(), registrationDTO.getPhoto());
        if (user != null) {
            UserRole userRole = userRoleService.getUserRoleByUserId(user.getId());
            String token = jwtUtils.generateToken(user);
            return new ResponseEntity<>(new UserResponse(token, user, userRole), HttpStatus.OK);
//            return ResponseEntity.ok("Регистрация завершена.");
        } else {
            return ResponseEntity.badRequest().body("Не удалось завершить регистрацию.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthDTO authDTO) {
        User user = authService.findByPhoneNumber(authDTO.getPhone());
        if (user != null) {
            // Если пользователь найден, отправляем код подтверждения
            authService.generateAndSendVerificationCode(user);
            return ResponseEntity.ok("Код подтверждения отправлен.");
        } else {
            // Если пользователя с таким номером телефона нет, возвращаем ошибку
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден.");
        }
    }

    // Эндпоинт для подтверждения кода и завершения процесса логина
    @PostMapping("/confirmLogin")
    public ResponseEntity<?> confirmLogin(@RequestBody VerificationDTO verificationDTO) {
        boolean isCodeValid = authService.verifyCode(verificationDTO.getPhoneNumber(), verificationDTO.getCode());
        if (isCodeValid) {
            // Загрузка UserDetails по номеру телефона
            UserDetails userDetails = userDetailsService.loadUserByUsername(verificationDTO.getPhoneNumber());
            if (userDetails != null) {
                // Генерация JWT токена для пользователя
                String token = jwtUtils.generateToken(userDetails);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не удалось загрузить детали пользователя.");
            }
        } else {
            return ResponseEntity.badRequest().body("Неверный код подтверждения или срок его действия истек.");
        }
    }

    /*@PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody AuthDTO authDTO) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred", HttpStatus.BAD_REQUEST);
        }
    }*/

    @PostMapping("/adminLogin")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        try {
            User user = authService.login(authDTO);
            if (user != null) {
                UserRole userRole = userRoleService.getUserRoleByUserId(user.getId());
                String token = jwtUtils.generateToken(user);
                return new ResponseEntity<Object>(new UserResponse(token, user, userRole), HttpStatus.OK);
            }
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(@Payload Map<String, String> payload) {
        String phone = payload.get("phone");
        User existingUser = authService.findIdByPhoneNumber(phone);
        if (existingUser != null) {
            return existingUser;
        } else {
            return null; // Или бросайте исключение, если пользователь не найден
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        List<User> users = authService.findConnectedUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/userId")
    public ResponseEntity<?> getUserIdByPhone(@RequestParam String phone) {
        Optional<Long> userId = authService.getUserIdByPhone(phone);
        if (userId.isPresent()) {
            return ResponseEntity.ok(Collections.singletonMap("userId", userId.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
