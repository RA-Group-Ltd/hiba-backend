package kz.wave.hiba.DTO;

import kz.wave.hiba.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private String name;
    private String email;
    private String phone;
    private Role role;

}
