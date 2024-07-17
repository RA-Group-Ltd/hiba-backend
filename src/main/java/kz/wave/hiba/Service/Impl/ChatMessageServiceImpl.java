package kz.wave.hiba.Service.Impl;

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

/**
 * Implementation of the {@link ChatMessageService} interface.
 */
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatService chatService;

    /**
     * Saves a chat message.
     *
     * @param chatMessage the chat message to be saved
     * @return the saved chat message
     * @throws IllegalArgumentException if the message content is empty or the chat is not specified
     */
    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        // Проверяем, содержит ли сообщение пустой текст
        if (chatMessage.getContent() == null || chatMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("Сообщение не может быть пустым");
        }

        // Проверяем, существует ли чат для данного сообщения
        if (chatMessage.getChat() == null) {
            throw new IllegalArgumentException("Не указан чат для сообщения");
        }

        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

    /**
     * Finds messages by chat ID.
     *
     * @param chatId the ID of the chat
     * @return a list of chat messages
     */
    @Override
    public List<ChatMessage> findMessagesByChatId(Long chatId) {
        return chatMessageRepository.findByChat(chatId);
    }

    /**
     * Retrieves messages by chat ID.
     *
     * @param chatId the ID of the chat
     * @return a list of chat messages
     */
    @Override
    public List<ChatMessage> getMessagesByChat(Long chatId) {
        return chatMessageRepository.findByChat(chatId);
    }

    /**
     * Retrieves unread messages for a given chat and recipient type.
     *
     * @param chatId the ID of the chat
     * @param recipientType the type of the recipient
     * @return a list of unread chat messages
     */
    @Override
    public List<ChatMessage> getUnreadMessages(Long chatId, SenderType recipientType) {
        return chatMessageRepository.findByChatAndSenderTypeAndMessageStatus(chatId, recipientType, MessageStatus.RECEIVED);
    }

    /**
     * Retrieves the last message in a chat.
     *
     * @param chatId the ID of the chat
     * @return an {@link Optional} containing the last chat message, if present
     */
    @Override
    public Optional<ChatMessage> getLastMessage(Long chatId) {
        return chatMessageRepository.findTopByChatOrderByTimestampDesc(chatId);
    }

    /**
     * Counts the number of unread messages for a given chat and recipient type.
     *
     * @param chatId the ID of the chat
     * @param recipientType the type of the recipient
     * @return the number of unread chat messages
     */
    @Override
    public long countUnreadMessages(Long chatId, SenderType recipientType) {
        return chatMessageRepository.countByChatAndSenderTypeAndMessageStatus(chatId, recipientType, MessageStatus.RECEIVED);
    }

    /**
     * Sets the status of a chat message.
     *
     * @param message the chat message
     * @param status the new status of the message
     */
    @Override
    public void setMessageStatus(ChatMessage message, MessageStatus status) {
        message.setMessageStatus(status);
        chatMessageRepository.save(message);
    }

    /**
     * Deletes a chat message.
     *
     * @param message the chat message to be deleted
     */
    @Override
    public void deleteMessage(ChatMessage message) {
        chatMessageRepository.delete(message);
    }
}
