package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    void deleteUserRoleByUserId(Long id);

    UserRole getByUserId(Long id);

}
