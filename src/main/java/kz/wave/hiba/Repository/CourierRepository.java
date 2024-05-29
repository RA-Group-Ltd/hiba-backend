package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Courier;
import kz.wave.hiba.Response.CourierOrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CourierRepository extends JpaRepository<Courier, Long> {

    Courier findCourierByUserNameOrUserPhone(String userName, String userPhone);
    Courier findByUserId(Long userId);

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
