package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.DTO.UserDTO;
import kz.wave.hiba.Entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUser();
    User getOneUser(Long id);
    User createUser(UserDTO userDTO);
    User login(AuthDTO authDTO);
    User updateUser(User user);
    void deleteUser(Long id);

}
