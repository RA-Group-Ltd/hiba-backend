package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Repository.ChatRepository;
import kz.wave.hiba.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat createChat(Long clientId, Long supportId) {
        Chat chat = new Chat();
        chat.setClientId(clientId);
        chat.setSupportId(supportId);
        chat.setArchive(false);
        chat.setRate(0);
        return chatRepository.save(chat);
    }

    @Override
    public Optional<Chat> getChatByClientAndSupport(Long clientId, Long supportId) {
        return chatRepository.findByClientIdAndSupportId(clientId, supportId);
    }

    @Override
    public List<Chat> getChatsByClientId(Long clientId) {
        return chatRepository.findByClientId(clientId);
    }

    @Override
    public List<Chat> getChatsBySupportId(Long supportId) {
        return chatRepository.findBySupportId(supportId);
    }

    @Override
    public void archiveChat(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        optionalChat.ifPresent(chat -> {
            chat.setArchive(true);
            chatRepository.save(chat);
        });
    }

    @Override
    public void rateChat(Long chatId, int rate) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        optionalChat.ifPresent(chat -> {
            chat.setRate(rate);
            chatRepository.save(chat);
        });
    }

    @Override
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    @Override
    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }
}
