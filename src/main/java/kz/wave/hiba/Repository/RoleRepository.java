package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Role} entities.
 */
@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role
     * @return the role with the specified name
     */
    Role findByName(String name);

    /**
     * Finds a role by its ID.
     *
     * @param id the ID of the role
     * @return the role with the specified ID
     */
    Role findRoleById(Long id);
}
