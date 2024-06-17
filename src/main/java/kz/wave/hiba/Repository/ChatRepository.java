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
            "WHERE c.clientId = :clientId AND c.isButchery = FALSE ")
    List<ChatHistoryResponse> findChatHistoryByClientId(@Param("clientId") Long clientId);
    List<Chat> findBySupportId(Long supportId);
//    List<Chat> findChatsBySupportIdIsNull();

    long countAllBySupportId(Long id);


    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, b) FROM Chat c " +
            "   LEFT JOIN Butchery b ON b.id = c.clientId " +
            "WHERE c.isButchery = TRUE AND c.archive = FALSE AND c.supportId IS NULL ")
    List<ChatHistoryResponse> findChatsBySupportIdIsNullAndIsButcheryAndArchiveIsFalse();

    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, u) FROM Chat c " +
            "   LEFT JOIN User u ON u.id = c.clientId " +
            "WHERE c.isButchery = FALSE AND c.archive = FALSE AND c.supportId IS NULL ")
    List<ChatHistoryResponse> findChatsBySupportIdIsNullAndIsButcheryFalseAndArchiveIsFalse();


    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, b) FROM Chat c " +
            "   LEFT JOIN Butchery b ON b.id = c.clientId " +
            "WHERE c.isButchery = TRUE AND c.archive = TRUE ")
    List<ChatHistoryResponse> findChatsByArchiveIsTrueAndIsButchery();
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, u) FROM Chat c " +
            "   LEFT JOIN User u ON u.id = c.clientId " +
            "WHERE c.isButchery = FALSE AND c.archive = TRUE ")
    List<ChatHistoryResponse> findChatsByArchiveIsTrueAndIsButcheryFalse();

    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, b) FROM Chat c " +
            "   LEFT JOIN Butchery b ON b.id = c.clientId " +
            "WHERE c.isButchery = TRUE AND c.archive = FALSE AND c.supportId IS NOT NULL")
    List<ChatHistoryResponse> findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButchery();

    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, u) FROM Chat c " +
            "   LEFT JOIN User u ON u.id = c.clientId " +
            "WHERE c.isButchery = FALSE AND c.archive = FALSE AND c.supportId IS NOT NULL")
    List<ChatHistoryResponse> findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButcheryFalse();



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

    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, s) FROM Chat c " +
            "   LEFT JOIN User s ON s.id = c.supportId " +
            "WHERE c.isButchery = TRUE AND c.clientId = :id ")
    List<ChatHistoryResponse> findChatsByButcheryId(@Param("id") Long id);

}
