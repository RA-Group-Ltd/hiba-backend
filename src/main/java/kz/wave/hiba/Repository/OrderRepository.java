package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Butcher;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Enum.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByButchery(Butchery butcheryId);
    List<Order> findOrdersByUserId(Long id);
    Order findOrderByUserId(Long id);
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY FIELD(o.orderStatus, 'DELIVERY_TOMORROW', 'ON_THE_WAY', 'PREPARING_FOR_DELIVERY', 'AWAITING_CONFIRMATION', 'DELIVERY_TOMORROW', 'IN_PROCESS', 'DELIVERED'), o.createdAt DESC")
    List<Order> findOrdersByUserIdSortedNatural(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus <> 'DELIVERED' " +
            "ORDER BY FIELD(o.orderStatus, 'DELIVERY_TOMORROW', 'ON_THE_WAY', 'PREPARING_FOR_DELIVERY', 'AWAITING_CONFIRMATION', 'IN_PROCESS'), o.createdAt DESC")
    List<Order> findOrdersByUserIdSortedActive(@Param("userId") Long userId);


    @Query("SELECT SUM(CASE WHEN o.isCharity = true THEN o.totalPrice ELSE o.donation END) " +
            "FROM Order o " +
            "WHERE o.createdAt >= :startDate AND o.orderStatus = kz.wave.hiba.Enum.OrderStatus.DELIVERED")
    int getDonationsByPeriod(@Param("startDate") Instant startDate);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.createdAt >= :startDate")
    int getPricesByPeriod(@Param("startDate") Instant startDate);

    @Query("SELECT COUNT(o) FROM Order o")
    long countOrders();


    @Query("SELECT COUNT(o) FROM Order o WHERE o.courier.id = :courierId AND o.orderStatus = kz.wave.hiba.Enum.OrderStatus.DELIVERED")
    long countDeliveredOrdersByCourierId(@Param("courierId") Long courierId);

    @Query("SELECT o FROM Order o " +
            "WHERE o.courier.id = :courierId " +
            "   AND o.orderStatus IN :statuses " +
            "   AND ( :startDate IS NULL OR o.createdAt >= :startDate) " +
            "   AND ( :endDate IS NULL OR  o.createdAt <= :endDate)")
    List<Order> getCourierOrders(@Param("courierId") Long courierId,
                                 @Param("statuses") List<String> statuses,
                                 @Param("startDate") Instant startDate,
                                 @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(o) FROM Order o where o.butchery = :butchery AND o.orderStatus != kz.wave.hiba.Enum.OrderStatus.DELIVERED")
    int getActiveOrdersByButchery(@Param("butchery") Butchery butchery);

    @Query("SELECT COUNT(o) FROM Order o where o.butchery = :butchery AND o.orderStatus = kz.wave.hiba.Enum.OrderStatus.DELIVERED")
    int getDeliveredOrdersByButchery(@Param("butchery") Butchery butchery);

    @Query("SELECT o FROM Order o " +
            "WHERE ((cast(o.id as string ) ILIKE '%' || :q || '%') OR (o.butchery.name ILIKE '%' || :q || '%')) " +
            "   AND ( :statuses IS NULL OR o.orderStatus IN :statuses ) " +
            "   AND ( :startDate IS NULL OR o.createdAt >= :startDate) " +
            "ORDER BY o.createdAt ASC")
    List<Order> findOrders(@Param("q") String query, @Param("startDate") Instant startDate, @Param("statuses")List<String> statuses);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.courier.id = :courierId AND o.orderStatus != kz.wave.hiba.Enum.OrderStatus.DELIVERED")
    Long countActiveOrdersByCourierId(@Param("courierId") Long courierId);

    @Query("SELECT SUM(o.totalPrice) " +
            "FROM Order o " +
            "WHERE o.createdAt >= :startDate AND o.orderStatus = kz.wave.hiba.Enum.OrderStatus.DELIVERED")
    int getTotalSumByPeriod(@Param("startDate") Instant startDate);
}
