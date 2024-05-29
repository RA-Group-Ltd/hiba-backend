package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Role;
import kz.wave.hiba.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhone(String phone);
    @Query("SELECT u FROM User u WHERE u.phone = :username OR u.email = :username")
    User findByUsername(@Param("username") String username);
    User findByRoles(User role);
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
    @Query("SELECT u.id FROM User u WHERE u.phone = :phone")
    Optional<Long> findIdByPhone(@Param("phone") String phone);


    @Query("SELECT u from User u " +
            "   JOIN UserRole ur on u = ur.user " +
            "   JOIN Role r on ur.role = r " +
            "WHERE (u.name ILIKE '%' || :q || '%' OR u.phone ILIKE '%' || :q || '%' )" +
            "   AND r.name IN :roles " +
            "ORDER BY " +
            "   CASE WHEN :sort = 'a-z' THEN u.name END ASC," +
            "   CASE WHEN :sort = 'z-a' THEN u.name END DESC")
    List<User> findEmployees(@Param("q") String q, @Param("roles") List<String> roles, @Param("sort") String sort);

    User findUserById(Long id);
}
