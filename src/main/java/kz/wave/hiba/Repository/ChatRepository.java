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

/**
 * Repository interface for {@link Chat} entities.
 */
@Repository
@Transactional
public interface ChatRepository extends JpaRepository<Chat, Long> {

    /**
     * Finds a chat by client ID and support ID.
     *
     * @param clientId the client ID
     * @param supportId the support ID
     * @return an {@link Optional} containing the chat if found, or empty if not found
     */
    Optional<Chat> findByClientIdAndSupportId(Long clientId, Long supportId);

    /**
     * Finds all chats by client ID.
     *
     * @param clientId the client ID
     * @return a list of chats associated with the client ID
     */
    List<Chat> findByClientId(Long clientId);

    /**
     * Finds chat history by client ID.
     *
     * @param clientId the client ID
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, s) FROM Chat c " +
            "   LEFT JOIN User s ON s.id = c.supportId " +
            "WHERE c.clientId = :clientId AND ( c.isButchery = FALSE OR c.isButchery IS NULL ) ")
    List<ChatHistoryResponse> findChatHistoryByClientId(@Param("clientId") Long clientId);

    /**
     * Finds all chats by support ID.
     *
     * @param supportId the support ID
     * @return a list of chats associated with the support ID
     */
    List<Chat> findBySupportId(Long supportId);

    /**
     * Counts all chats by support ID.
     *
     * @param id the support ID
     * @return the number of chats associated with the support ID
     */
    long countAllBySupportId(Long id);

    /**
     * Finds chats by support ID is null, is butchery, and archive is false.
     *
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, b) FROM Chat c " +
            "   LEFT JOIN Butchery b ON b.id = c.clientId " +
            "WHERE c.isButchery = TRUE AND c.archive = FALSE AND c.supportId IS NULL ")
    List<ChatHistoryResponse> findChatsBySupportIdIsNullAndIsButcheryAndArchiveIsFalse();

    /**
     * Finds chats by support ID is null, is not butchery, and archive is false.
     *
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, u) FROM Chat c " +
            "   LEFT JOIN User u ON u.id = c.clientId " +
            "WHERE c.isButchery = FALSE AND c.archive = FALSE AND c.supportId IS NULL ")
    List<ChatHistoryResponse> findChatsBySupportIdIsNullAndIsButcheryFalseAndArchiveIsFalse();

    /**
     * Finds chats by archive is true and is butchery.
     *
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, b) FROM Chat c " +
            "   LEFT JOIN Butchery b ON b.id = c.clientId " +
            "WHERE c.isButchery = TRUE AND c.archive = TRUE ")
    List<ChatHistoryResponse> findChatsByArchiveIsTrueAndIsButchery();

    /**
     * Finds chats by archive is true and is not butchery.
     *
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, u) FROM Chat c " +
            "   LEFT JOIN User u ON u.id = c.clientId " +
            "WHERE c.isButchery = FALSE AND c.archive = TRUE ")
    List<ChatHistoryResponse> findChatsByArchiveIsTrueAndIsButcheryFalse();

    /**
     * Finds chats by archive is false, support ID is not null, and is butchery.
     *
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, b) FROM Chat c " +
            "   LEFT JOIN Butchery b ON b.id = c.clientId " +
            "WHERE c.isButchery = TRUE AND c.archive = FALSE AND c.supportId IS NOT NULL")
    List<ChatHistoryResponse> findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButchery();

    /**
     * Finds chats by archive is false, support ID is not null, and is not butchery.
     *
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, u) FROM Chat c " +
            "   LEFT JOIN User u ON u.id = c.clientId " +
            "WHERE c.isButchery = FALSE AND c.archive = FALSE AND c.supportId IS NOT NULL")
    List<ChatHistoryResponse> findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButcheryFalse();

    /**
     * Finds chats by support ID and various filters.
     *
     * @param supportId the support ID
     * @param filter the filter for rates
     * @param startDate the start date filter
     * @param endDate the end date filter
     * @return a list of support chat responses
     */
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

    /**
     * Finds chats by butchery ID.
     *
     * @param id the butchery ID
     * @return a list of chat history responses
     */
    @Query("SELECT new kz.wave.hiba.Response.ChatHistoryResponse(c, s) FROM Chat c " +
            "   LEFT JOIN User s ON s.id = c.supportId " +
            "WHERE c.isButchery = TRUE AND c.clientId = :id ")
    List<ChatHistoryResponse> findChatsByButcheryId(@Param("id") Long id);
}
