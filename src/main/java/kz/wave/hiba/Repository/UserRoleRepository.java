package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link UserRole} entities.
 */
@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * Deletes a user role by user ID.
     *
     * @param id the user ID
     */
    void deleteUserRoleByUserId(Long id);

    /**
     * Gets a user role by user ID.
     *
     * @param id the user ID
     * @return the user role associated with the specified user ID
     */
    UserRole getUserRoleByUserId(Long id);

    /**
     * Gets a user role by user ID.
     *
     * @param id the user ID
     * @return the user role associated with the specified user ID
     */
    UserRole getByUserId(Long id);
}
