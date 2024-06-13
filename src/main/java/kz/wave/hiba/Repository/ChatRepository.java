package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Response.ChatHistoryResponse;
import kz.wave.hiba.Response.SupportChatResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByClientIdAndSupportId(Long clientId, Long supportId);
//    Optional<Chat> findByClient
    List<Chat> findByClientId(Long clientId);

    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, s) FROM Chat c " +
            "   LEFT JOIN User s ON s.id = c.supportId " +
            "WHERE c.clientId = :clientId")
    List<ChatHistoryResponse> findChatHistoryByClientId(@Param("clientId") Long clientId);
    List<Chat> findBySupportId(Long supportId);
//    List<Chat> findChatsBySupportIdIsNull();

    long countAllBySupportId(Long id);

    List<Chat> findChatsBySupportIdIsNullAndIsButcheryAndArchiveIsFalse(boolean isButchery);
    List<Chat> findChatsByArchiveIsTrueAndIsButchery(boolean isButchery);
    List<Chat> findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButchery(boolean isButchery);
    @Query("SELECT new kz.wave.hiba.Response.SupportChatResponse(c.id, u, c.createdAt, c.rate) FROM Chat c " +
            "   LEFT JOIN User u ON c.clientId = u.id " +
            "WHERE c.supportId = :supportId " +
            "   AND CAST(c.rate AS STRING) IN :rates " +
            "   AND ( :startDate IS NULL OR c.createdAt >= :startDate) " +
            "   AND ( :endDate IS NULL OR  c.createdAt <= :endDate)")
    List<SupportChatResponse> findChatsBySupportId(@Param("supportId") Long supportId,
                                                   @Param("rates") List<String> filter,
                                                   @Param("startDate") Instant startDate,
                                                   @Param("endDate") Instant endDate);

    @Query("SELECT c FROM Chat c " +
            "   WHERE c.isButchery = TRUE AND c.clientId = :id")
    List<Chat> findChatsByButcheryId(@Param("id") Long id);
}
