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

//        var chatId = chatService.getChatByClientAndSupport(chatMessage.getChat().getClientId(), chatMessage.getChat().getSupportId()).orElseThrow();
//        chatMessage.setChat(chatId);
        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

    @Override
    public List<ChatMessage> findMessagesByChatId(Long chatId) {
        return chatMessageRepository.findByChat(chatId);
    }

    @Override
    public List<ChatMessage> getMessagesByChat(Long chatId) {
        return chatMessageRepository.findByChat(chatId);
    }

    @Override
    public List<ChatMessage> getUnreadMessages(Long chatId, SenderType recipientType) {
        return chatMessageRepository.findByChatAndSenderTypeAndMessageStatus(chatId, recipientType, MessageStatus.RECEIVED);
    }

    @Override
    public Optional<ChatMessage> getLastMessage(Long chatId) {
        return chatMessageRepository.findTopByChatOrderByTimestampDesc(chatId);
    }

    @Override
    public long countUnreadMessages(Long chatId, SenderType recipientType) {
        return chatMessageRepository.countByChatAndSenderTypeAndMessageStatus(chatId, recipientType, MessageStatus.RECEIVED);
    }

    @Override
    public void setMessageStatus(ChatMessage message, MessageStatus status) {
        message.setMessageStatus(status);
        chatMessageRepository.save(message);
    }

    @Override
    public void deleteMessage(ChatMessage message) {
        chatMessageRepository.delete(message);
    }
}
