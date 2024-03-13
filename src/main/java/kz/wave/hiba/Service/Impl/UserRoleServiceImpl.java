package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.UserRole;
import kz.wave.hiba.Repository.UserRoleRepository;
import kz.wave.hiba.Service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole getUserRoleByUserId(Long id) {
        return userRoleRepository.getByUserId(id);
    }
}
