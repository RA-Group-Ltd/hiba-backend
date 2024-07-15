package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;

import java.util.List;
import java.util.Optional;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);
    public List<ChatMessage> findMessagesByChatId(Long chatId);
    List<ChatMessage> getMessagesByChat(Long chatId);
    List<ChatMessage> getUnreadMessages(Long chatId, SenderType recipientType);
    Optional<ChatMessage> getLastMessage(Long chatId);
    long countUnreadMessages(Long chatId, SenderType recipientType);
    void setMessageStatus(ChatMessage message, MessageStatus status);
    void deleteMessage(ChatMessage message);

}
