package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their phone number.
     *
     * @param phone the phone number of the user
     * @return the user with the specified phone number
     */
    User findByPhone(String phone);

    /**
     * Finds a user by their username (either phone or email).
     *
     * @param username the username (phone or email) of the user
     * @return the user with the specified username
     */
    @Query("SELECT u FROM User u WHERE u.phone = :username OR u.email = :username")
    User findByUsername(@Param("username") String username);

    /**
     * Finds a user by their roles.
     *
     * @param role the role of the user
     * @return the user with the specified roles
     */
    User findByRoles(User role);

    /**
     * Counts the total number of users.
     *
     * @return the total number of users
     */
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();

    /**
     * Finds the ID of a user by their phone number.
     *
     * @param phone the phone number of the user
     * @return an {@link Optional} containing the ID of the user if found, or empty if not found
     */
    @Query("SELECT u.id FROM User u WHERE u.phone = :phone")
    Optional<Long> findIdByPhone(@Param("phone") String phone);

    /**
     * Finds employees by search query, roles, and sort order.
     *
     * @param q the search query
     * @param roles the list of roles to filter by
     * @param sort the sort order
     * @return a list of users matching the search query and filters
     */
    @Query("SELECT u from User u " +
            "   JOIN UserRole ur on u = ur.user " +
            "   JOIN Role r on ur.role = r " +
            "WHERE (u.name ILIKE '%' || :q || '%' OR u.phone ILIKE '%' || :q || '%' )" +
            "   AND r.name IN :roles " +
            "ORDER BY " +
            "   CASE WHEN :sort = 'a-z' THEN u.name END ASC," +
            "   CASE WHEN :sort = 'z-a' THEN u.name END DESC")
    List<User> findEmployees(@Param("q") String q, @Param("roles") List<String> roles, @Param("sort") String sort);

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID
     */
    User findUserById(Long id);
}
