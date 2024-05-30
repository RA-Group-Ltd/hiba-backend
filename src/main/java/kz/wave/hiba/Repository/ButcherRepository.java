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

@Repository
@Transactional
public interface ButcherRepository extends JpaRepository<Butcher, Long> {

    /*@Query("SELECT b FROM Butchery b WHERE " +
            "(:q IS NULL OR b.name LIKE CONCAT('%', :q, '%')) " +
            "AND (:categories IS EMPTY OR b.category.id IN :categories) " +
            "ORDER BY b.name")
    List<Butchery> findByCriteria(@Param("q") String q, @Param("categories") List<Integer> categories, @Param("latitude") Float latitude, @Param("longitude") Float longitude, @Param("sort") String sort);
*/

    @Query("SELECT b.user FROM Butcher b WHERE b.butchery = :id")
    List<User> findAllUsersByButchery(@Param("id") Butchery id);

    @Query("SELECT b.butchery FROM Butcher b WHERE b.user.id = :id")
    Butchery findButcheryByUserId(@Param("id") Long userId);

    Butcher findByUserId(Long userId);

}
