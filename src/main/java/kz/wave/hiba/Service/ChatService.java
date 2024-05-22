package kz.wave.hiba.Service;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.SenderType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    Optional<Chat> getChatById(Long id);
    Chat createChat(Long orderId, HttpServletRequest request);
    Chat startDialog(Long chatId, HttpServletRequest request);
    ResponseEntity<?> completeDialog(Long chatId, HttpServletRequest request);
    List<Chat> getChatsByClientId(Long clientId);
    List<Chat> getChatsBySupportId(Long supportId);
    void archiveChat(Long chatId);
    void rateChat(Long chatId, int rate);
    List<Chat> getAllChats();
    List<Chat> getChats(boolean isButchery, String type);
}
