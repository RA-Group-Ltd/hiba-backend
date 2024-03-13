package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.AuthCheckDTO;
import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.VerificationCode;
import org.springframework.web.multipart.MultipartFile;

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
    void confirmUser(String phoneNumber);
    User completeRegistration(String phoneNumber, String name, MultipartFile photo);

}
