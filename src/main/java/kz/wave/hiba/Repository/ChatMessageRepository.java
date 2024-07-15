package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link ChatMessage} entities.
 */
@Repository
@Transactional
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Counts the number of chat messages in a specific chat by a specific sender type.
     *
     * @param chat the chat ID
     * @param senderType the sender type
     * @return the number of chat messages
     */
    long countByChatAndSenderType(Long chat, SenderType senderType);

    /**
     * Finds all chat messages in a specific chat.
     *
     * @param chat the chat ID
     * @return a list of chat messages
     */
    List<ChatMessage> findByChat(Long chat);

    /**
     * Finds chat messages in a specific chat by sender type and message status.
     *
     * @param chat the chat ID
     * @param senderType the sender type
     * @param messageStatus the message status
     * @return a list of chat messages
     */
    List<ChatMessage> findByChatAndSenderTypeAndMessageStatus(Long chat, SenderType senderType, MessageStatus messageStatus);

    /**
     * Finds the most recent chat message in a specific chat.
     *
     * @param chat the chat ID
     * @return an {@link Optional} containing the most recent chat message if found, or empty if not found
     */
    Optional<ChatMessage> findTopByChatOrderByTimestampDesc(Long chat);

    /**
     * Counts the number of chat messages in a specific chat by sender type and message status.
     *
     * @param chat the chat ID
     * @param senderType the sender type
     * @param messageStatus the message status
     * @return the number of chat messages
     */
    long countByChatAndSenderTypeAndMessageStatus(Long chat, SenderType senderType, MessageStatus messageStatus);
}
