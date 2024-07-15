package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.EmployeeDTO;
import kz.wave.hiba.Entities.User;

import java.util.List;

public interface EmployeesService {
    List<User> getEmployees(String sort, List<String> filter, String query);
    User createEmployee(EmployeeDTO employeeDTO);
    User getEmployee(Long id);
}
