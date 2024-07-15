package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Butcher;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Butcher} entities.
 */
@Repository
@Transactional
public interface ButcherRepository extends JpaRepository<Butcher, Long> {

    /**
     * Finds a butcher by the user ID.
     *
     * @param user_id the ID of the user
     * @return the butcher associated with the user ID
     */
    Butcher findByUserId(Long user_id);

    /**
     * Finds all users associated with a specific butchery.
     *
     * @param id the butchery ID
     * @return a list of users associated with the butchery
     */
    @Query("SELECT b.user FROM Butcher b WHERE b.butchery = :id")
    List<User> findAllUsersByButchery(@Param("id") Butchery id);

    /**
     * Finds the butchery associated with a specific user ID.
     *
     * @param userId the user ID
     * @return the butchery associated with the user ID
     */
    @Query("SELECT b.butchery FROM Butcher b WHERE b.user.id = :id")
    Butchery findButcheryByUserId(@Param("id") Long userId);

}
