package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.TelegramBot;
import kz.wave.hiba.DTO.AuthCheckDTO;
import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.RoleRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Repository.UserRoleRepository;
import kz.wave.hiba.Repository.VerificationCodeRepository;
import kz.wave.hiba.Service.AuthService;
import kz.wave.hiba.Service.TelegramService;
import kz.wave.hiba.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final TelegramBot telegramBot;

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber);
    }

    @Override
    public User createUserWithPhoneNumber(String phoneNumber) {
        User newUser = new User();
        newUser.setPhone(phoneNumber);
        newUser.setTelegramChatId(newUser.getTelegramChatId());
        return userRepository.save(newUser);
    }

    @Override
    public void generateAndSendVerificationCode(User user) {
        VerificationCode verificationCode = generateVerificationCode(user);
        verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode generateVerificationCode(User user) {
        VerificationCode code = new VerificationCode();
        String verificationCode = String.format("%04d", new Random().nextInt(10000)); // генерирует 4-значный код
        code.setToken(verificationCode);
        // Сетим expirationDate, используя LocalDateTime
        code.setExpirationDate(LocalDateTime.now().plusMinutes(10)); // код действителен 10 минут
        code.setUser(user);
        return code;
    }

    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        // Найдем пользователя по номеру телефона
        User user = findByPhoneNumber(phoneNumber);
        if (user == null) {
            return false;
        }

        // Найдем код подтверждения для пользователя, который еще не истек
        List<VerificationCode> validCodes = verificationCodeRepository.findByUserIdAndExpirationDateAfter(user.getId(), LocalDateTime.now());

        // Проверим, совпадает ли предоставленный код с одним из непросроченных кодов
        for (VerificationCode validCode : validCodes) {
            if (validCode.getToken().equals(code)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void confirmUser(String phoneNumber) {
        User user = findByPhoneNumber(phoneNumber);
        if (user != null) {
            user.setConfirmed(true); // Предполагается, что у вас есть поле confirmed в сущности User
            userRepository.save(user);
        }
    }

    @Override
    public User completeRegistration(String phoneNumber, String name, MultipartFile photo) {
        // Находим пользователя по номеру телефона
        User user = userRepository.findByPhone(phoneNumber);
        if (user != null) {
            user.setName(name);
            user.setCreatedAt(Instant.now());
            // Здесь логика для сохранения фотографии, если применимо
            // Например, сохранение файла на сервере или в облачном хранилище и установка пути к файлу в поле user.photoPath

            Role role = roleRepository.findByName("ROLE_USER");
            UserRoleId userRoleId = new UserRoleId(user.getId(), role.getId());
            UserRole userRole = new UserRole(userRoleId, user, role);
            userRoleRepository.save(userRole);

            userRepository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public boolean checkPhone(AuthCheckDTO authCheckDTO) {
        return userRepository.findByPhone(authCheckDTO.getPhone()) != null;
    }

    @Override
    public User registerUser(AuthDTO authDTO) {
        if (userRepository.findByPhone(authDTO.getPhone()) != null) {
            return null;
        }

        User newUser = new User();
        newUser.setPhone(authDTO.getPhone());
//        newUser.setName(authDTO.getUsername());
        newUser.setCreatedAt(Instant.now());

        userRepository.save(newUser);

        Role role = roleRepository.findByName("ROLE_USER");
        UserRoleId userRoleId = new UserRoleId(newUser.getId(), role.getId());
        UserRole userRole = new UserRole(userRoleId, newUser, role);
        userRoleRepository.save(userRole);

        return newUser;
    }

    @Override
    public User login(AuthDTO authDTO) {
        User user = userRepository.findByPhone(authDTO.getPhone());
        if (user != null) {
//            if (passwordEncoder.matches(authDTO.getCode(), user.getPassword())) {
                return user;
//            }
        }

        return null;
    }
}
