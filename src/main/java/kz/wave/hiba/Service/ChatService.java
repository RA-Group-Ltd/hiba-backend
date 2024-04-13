package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    Optional<Chat> getChatById(Long id);
    Chat createChat(Long clientId, Long supportId);
    Optional<Chat> getChatByClientAndSupport(Long clientId, Long supportId);
    List<Chat> getChatsByClientId(Long clientId);
    List<Chat> getChatsBySupportId(Long supportId);
    void archiveChat(Long chatId);
    void rateChat(Long chatId, int rate);
    List<Chat> getAllChats();

}
