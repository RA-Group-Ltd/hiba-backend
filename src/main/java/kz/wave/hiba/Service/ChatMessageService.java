package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;

import java.util.List;
import java.util.Optional;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);
    List<ChatMessage> getMessagesByChat(Chat chat);
    List<ChatMessage> getUnreadMessages(Chat chat, SenderType recipientType);
    Optional<ChatMessage> getLastMessage(Chat chat);
    long countUnreadMessages(Chat chat, SenderType recipientType);
    void setMessageStatus(ChatMessage message, MessageStatus status);
    void deleteMessage(ChatMessage message);

}
