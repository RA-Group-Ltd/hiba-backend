package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Butchery} entities.
 */
@Repository
@Transactional
public interface ButcheryRepository extends JpaRepository<Butchery, Long> {

    /**
     * Finds all butcheries that match the given specification and sorts them according to the specified order.
     *
     * @param spec the specification to filter the butcheries
     * @param sortOrder the sort order
     * @return a list of butcheries matching the specification
     */
    List<Butchery> findAll(Specification<Butchery> spec, Sort sortOrder);

    /**
     * Counts the total number of butcheries.
     *
     * @return the total number of butcheries
     */
    @Query("SELECT count(b) from Butchery b")
    long countButcheries();

    /**
     * Finds butcheries that match the search query and city filters, and sorts them according to the specified sort order.
     *
     * @param sort the sort order
     * @param q the search query
     * @param cityList the list of city IDs to filter by
     * @return a list of butcheries matching the search query and filters
     */
    @Query("SELECT b FROM Butchery b " +
            "WHERE ((b.name ILIKE '%' || :q || '%') OR (b.email ILIKE '%' || :q || '%' ) OR b.id = cast(:q as INTEGER)) " +
            "   AND ( :cities IS NULL OR b.city.id IN :cities) " +
            "ORDER BY " +
            "   CASE WHEN :sort = 'a-z' THEN b.name END ASC," +
            "   CASE WHEN :sort = 'z-a' THEN b.name END DESC,  " +
            "   CASE WHEN :sort = 'new' THEN b.createdAt END ASC," +
            "   CASE WHEN :sort = 'old' THEN b.createdAt END DESC")
    List<Butchery> findButcheries(@Param("sort") String sort, @Param("q") String q, @Param("cities") List<Long> cityList);

    /**
     * Finds a butchery by the owner.
     *
     * @param owner the owner of the butchery
     * @return the butchery associated with the owner
     */
    Butchery findButcheryByOwner(User owner);
}
