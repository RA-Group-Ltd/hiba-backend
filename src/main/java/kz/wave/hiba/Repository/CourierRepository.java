package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Courier;
import kz.wave.hiba.Response.CourierOrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Courier} entities.
 */
@Repository
@Transactional
public interface CourierRepository extends JpaRepository<Courier, Long> {

    /**
     * Finds a courier by username or user phone.
     *
     * @param userName the username of the courier
     * @param userPhone the phone number of the courier
     * @return the courier with the specified username or phone number
     */
    Courier findCourierByUserNameOrUserPhone(String userName, String userPhone);

    /**
     * Finds a courier by user ID.
     *
     * @param userId the user ID
     * @return the courier with the specified user ID
     */
    Courier findByUserId(Long userId);

    /**
     * Finds couriers by search query, sort order, and city filters, and returns a list of courier order responses.
     *
     * @param query the search query
     * @param sort the sort order
     * @param cityList the list of city IDs to filter by
     * @return a list of courier order responses matching the search query and filters
     */
    @Query("SELECT new kz.wave.hiba.Response.CourierOrderResponse(c.id, c.user.name, c.user.phone, c.user.email, c.city, COALESCE(COUNT(o.id), 0)) FROM Courier c " +
            "   LEFT JOIN Order o ON o.courier.id = c.id " +
            "WHERE ((c.user.name ILIKE '%' || :q || '%') OR (c.user.email ILIKE '%' || :q || '%' ) OR (c.user.phone ILIKE '%' || :q || '%' )) " +
            "   AND ( :cities IS NULL OR c.city.id IN :cities) " +
            "GROUP BY c.id, c.user.name, c.user.phone, c.user.email, c.city " +
            "ORDER BY " +
            "   CASE WHEN :sort = 'a-z' THEN c.user.name END ASC," +
            "   CASE WHEN :sort = 'z-a' THEN c.user.name END DESC")
    List<CourierOrderResponse> findCouriers(@Param("q") String query, @Param("sort") String sort, @Param("cities") List<Long> cityList);
}
