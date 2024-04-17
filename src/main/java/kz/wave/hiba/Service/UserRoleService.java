package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Role;
import kz.wave.hiba.Entities.UserRole;

public interface UserRoleService {

    UserRole getUserRoleByUserId(Long id);
    UserRole identifyUserRole(Role role);

}
