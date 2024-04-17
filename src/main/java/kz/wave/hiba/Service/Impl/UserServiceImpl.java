package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.AuthDTO;
import kz.wave.hiba.DTO.UserDTO;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

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
//        newUser.setAvatar(userDTO.getAvatar());

        return userRepository.save(newUser);
    }

    @Override
    public User login(AuthDTO authDTO) {
        User user = userRepository.findByPhone(authDTO.getPhone());
        if (user != null) {
            if (passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }
}
