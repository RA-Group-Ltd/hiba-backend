package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Config.MailingUtils;
import kz.wave.hiba.DTO.EmployeeDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.RoleRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Repository.UserRoleRepository;
import kz.wave.hiba.Service.EmployeesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link EmployeesService} interface.
 */
@Service
@RequiredArgsConstructor
public class EmployeesServiceImpl implements EmployeesService {
    Logger logger = LoggerFactory.getLogger(EmployeesServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private MailingUtils mailingUtils;

    /**
     * Retrieves a list of employees based on sorting, filtering, and search query.
     *
     * @param sort the sorting order
     * @param filter the filtering criteria
     * @param query the search query
     * @return a list of employees matching the criteria
     */
    @Override
    public List<User> getEmployees(String sort, List<String> filter, String query) {
        return userRepository.findEmployees(query, filter, sort);
    }

    /**
     * Creates a new employee.
     *
     * @param employeeDTO the data transfer object containing employee creation data
     * @return the created employee, or null if the role is not allowed
     */
    @Override
    public User createEmployee(EmployeeDTO employeeDTO) {
        List<String> allowedRoles = Arrays.asList("ROLE_ADMIN", "ROLE_SUPPORT");
        if (!allowedRoles.contains(employeeDTO.getRole().getName())) {
            return null;
        }
        User newEmployee = new User();

        // Check if user with the given phone already exists
        User existingUser = userRepository.findByPhone(employeeDTO.getPhone());
        if (existingUser == null) {
            String newPassword = generatePassword();

            // Create a new User
            User user = new User();
            user.setName(employeeDTO.getName());
            user.setPhone(employeeDTO.getPhone());
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setEmail(employeeDTO.getEmail());
            user.setCreatedAt(Instant.now());

            // Save the user to generate ID
            newEmployee = userRepository.save(user);

            logger.debug("Employee role: " + employeeDTO.getRole());

            // Assign role to the user
            Role role = roleRepository.findByName(employeeDTO.getRole().getName());
            UserRoleId userRoleId = new UserRoleId(newEmployee.getId(), role.getId());
            UserRole userRole = new UserRole(userRoleId, newEmployee, role);
            userRoleRepository.save(userRole);

            mailingUtils.sendPass(employeeDTO.getEmail(), newPassword);
        }

        // Save and return the employee
        return newEmployee;
    }

    /**
     * Retrieves an employee by its ID.
     *
     * @param id the ID of the employee
     * @return the employee found by ID
     */
    @Override
    public User getEmployee(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    /**
     * Generates a random password.
     *
     * @return a random password
     */
    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
