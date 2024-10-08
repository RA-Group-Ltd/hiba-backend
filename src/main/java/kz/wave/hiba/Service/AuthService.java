package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.AuthCheckDTO;
import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.VerificationCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AuthService {

    boolean checkPhone(AuthCheckDTO authCheckDTO);
    User registerUser(AuthDTO authDTO);
    User login(AuthDTO authDTO);
    VerificationCode generateVerificationCode(User user);
    void generateAndSendVerificationCode(User user);
//    void generateAndSendVerificationCode(User user);
    User findByPhoneNumber(String phoneNumber);
    User createUserWithPhoneNumber(String phoneNumber);
    boolean verifyCode(String phoneNumber, String code);
    ResponseEntity<?> confirmUser(String phoneNumber);
    User completeRegistration(String phoneNumber, String name, MultipartFile photo);
    boolean isValidTokenForUser(String token, String username);
    public List<User> findConnectedUsers();
    public User findIdByPhoneNumber(String phone);
    public long quantityOfUsers();
    public Optional<Long> getUserIdByPhone(String phone);

}
