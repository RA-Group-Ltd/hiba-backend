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

    long countByChatAndSenderType(Long chat, SenderType senderType);
//    List<ChatMessage> findByChat(Long chatId);

    List<ChatMessage> findByChat(Long chat);

    List<ChatMessage> findByChatAndSenderTypeAndMessageStatus(Long chat, SenderType senderType, MessageStatus messageStatus);

    Optional<ChatMessage> findTopByChatOrderByTimestampDesc(Long chat);

    long countByChatAndSenderTypeAndMessageStatus(Long chat, SenderType senderType, MessageStatus messageStatus);
}
