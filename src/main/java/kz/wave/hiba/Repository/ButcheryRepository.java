package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ButcheryRepository extends JpaRepository<Butchery, Long> {
    List<Butchery> findAll(Specification<Butchery> spec, Sort sortOrder);

    @Query("SELECT count(b) from Butchery b")
    long countButcheries();

    @Query("SELECT b FROM Butchery b " +
            "WHERE ((b.name ILIKE '%' || :q || '%') OR (b.email ILIKE '%' || :q || '%' ) OR b.id = cast(:q as INTEGER)) " +
            "   AND ( :cities IS NULL OR b.city.id IN :cities) " +
            "ORDER BY " +
            "   CASE WHEN :sort = 'a-z' THEN b.name END ASC," +
            "   CASE WHEN :sort = 'z-a' THEN b.name END DESC,  " +
            "   CASE WHEN :sort = 'new' THEN b.createdAt END ASC," +
            "   CASE WHEN :sort = 'old' THEN b.createdAt END DESC")
    List<Butchery> findButcheries(@Param("sort") String sort, @Param("q") String q, @Param("cities") List<Long> cityList);

    Butchery findButcheryByOwner(User owner);
}
