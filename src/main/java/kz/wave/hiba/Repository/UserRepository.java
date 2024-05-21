package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhone(String phone);
    User findByRoles(User role);
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
    @Query("SELECT u.id FROM User u WHERE u.phone = :phone")
    Optional<Long> findIdByPhone(@Param("phone") String phone);

}
