package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;
import kz.wave.hiba.Repository.ChatMessageRepository;
import kz.wave.hiba.Service.ChatMessageService;
import kz.wave.hiba.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatService chatService;

    public ChatMessage save(ChatMessage chatMessage) {
        // Проверяем, содержит ли сообщение пустой текст
        if (chatMessage.getContent() == null || chatMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("Сообщение не может быть пустым");
        }

        // Проверяем, существует ли чат для данного сообщения
        if (chatMessage.getChat() == null) {
            throw new IllegalArgumentException("Не указан чат для сообщения");
        }

        // Сохраняем сообщение в базе данных
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getMessagesByChat(Chat chat) {
        return chatMessageRepository.findByChat(chat);
    }

    @Override
    public List<ChatMessage> getUnreadMessages(Chat chat, SenderType recipientType) {
        return chatMessageRepository.findByChatAndRecipientTypeAndStatus(chat, recipientType, MessageStatus.RECEIVED);
    }

    @Override
    public Optional<ChatMessage> getLastMessage(Chat chat) {
        return chatMessageRepository.findTopByChatOrderByTimestampDesc(chat);
    }

    @Override
    public long countUnreadMessages(Chat chat, SenderType recipientType) {
        return chatMessageRepository.countByChatAndRecipientTypeAndStatus(chat, recipientType, MessageStatus.RECEIVED);
    }

    @Override
    public void setMessageStatus(ChatMessage message, MessageStatus status) {
        message.setStatus(status);
        chatMessageRepository.save(message);
    }

    @Override
    public void deleteMessage(ChatMessage message) {
        chatMessageRepository.delete(message);
    }
}
