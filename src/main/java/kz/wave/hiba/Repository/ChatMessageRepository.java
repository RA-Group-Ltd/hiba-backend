package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    long countByChatAndRecipientType(Chat chat, SenderType recipientType);

    List<ChatMessage> findByChat(Chat chat);

    List<ChatMessage> findByChatAndRecipientTypeAndStatus(Chat chat, SenderType recipientType, MessageStatus messageStatus);

    Optional<ChatMessage> findTopByChatOrderByTimestampDesc(Chat chat);

    long countByChatAndRecipientTypeAndStatus(Chat chat, SenderType recipientType, MessageStatus messageStatus);
}
