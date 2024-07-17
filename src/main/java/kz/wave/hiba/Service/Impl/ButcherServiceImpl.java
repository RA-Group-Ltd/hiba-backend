package kz.wave.hiba.Service.Impl;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Config.MailingUtils;
import kz.wave.hiba.DTO.ButcherEmployeeCreateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.*;
import kz.wave.hiba.Service.ButcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the {@link ButcherService} interface.
 */
@Service
@RequiredArgsConstructor
public class ButcherServiceImpl implements ButcherService {

    private final ButcherRepository butcherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailingUtils mailingUtils;
    private final ButcheryRepository butcheryRepository;

    /**
     * Deletes a butcher by user ID.
     *
     * @param id the user ID of the butcher to be deleted
     * @param butcheryOwner the owner of the butchery
     */
    @Override
    public void deleteButcherByUserId(Long id, User butcheryOwner) {
        try {
            Butchery ownerButchery = butcheryRepository.findButcheryByOwner(butcheryOwner);
            Butchery userButchery = butcherRepository.findButcheryByUserId(id);
            if (ownerButchery != null && userButchery != null && Objects.equals(ownerButchery.getId(), userButchery.getId())) {
                Butcher butcher = butcherRepository.findByUserId(id);
                butcherRepository.deleteById(butcher.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Gets the butchery ID by butcher user ID.
     *
     * @param userId the user ID of the butcher
     * @return the butchery ID
     */
    @Override
    public Long getButcheryIdByButhcerUserId(Long userId) {
        Butchery butchery = butcherRepository.findButcheryByUserId(userId);
        if (butchery == null) {
            return null;
        }
        return butchery.getId();
    }

    /**
     * Adds a new butcher employee.
     *
     * @param butcherEmployeeCreateDTO the butcher employee creation data transfer object
     * @param currentUser the current user
     * @return the added butcher employee
     */
    @Transactional
    public Butcher addButcherEmployee(ButcherEmployeeCreateDTO butcherEmployeeCreateDTO, User currentUser) {
        Optional<User> user = Optional.ofNullable(userRepository.findByPhone(butcherEmployeeCreateDTO.getPhone()));

        if (user.isPresent()) {
            return null;
        }

        User newButcherEmployee = new User();
        newButcherEmployee.setName(butcherEmployeeCreateDTO.getName());
        newButcherEmployee.setPhone(butcherEmployeeCreateDTO.getPhone());
        newButcherEmployee.setEmail(butcherEmployeeCreateDTO.getEmail());
        String genPassword = generatePassword();
        newButcherEmployee.setPassword(passwordEncoder.encode(genPassword));
        newButcherEmployee.setCreatedAt(Instant.now());

        User savedUser = userRepository.save(newButcherEmployee);

        Role role = roleRepository.findByName("ROLE_BUTCHERY_EMPLOYEE");
        UserRoleId userRoleId = new UserRoleId(newButcherEmployee.getId(), role.getId());
        UserRole userRole = new UserRole(userRoleId, newButcherEmployee, role);
        userRoleRepository.save(userRole);

        Butchery butchery = butcheryRepository.findButcheryByOwner(currentUser);
        Butcher butcher = new Butcher();
        butcher.setButchery(butchery);
        butcher.setUser(savedUser);

        mailingUtils.sendPass(newButcherEmployee.getEmail(), genPassword);

        return butcherRepository.save(butcher);
    }

    /**
     * Generates a random password.
     *
     * @return the generated password
     */
    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
