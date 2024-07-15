package kz.wave.hiba.Service;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Response.ChatHistoryResponse;
import kz.wave.hiba.Response.SupportChatResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    Optional<Chat> getChatById(Long id);
    ChatHistoryResponse createChat(Long orderId, HttpServletRequest request);
    ChatHistoryResponse createButcheryChat(HttpServletRequest request);
    Chat startDialog(Long chatId, HttpServletRequest request);
    Chat startDialog(Long chatId);
    ResponseEntity<?> completeDialog(Long chatId, HttpServletRequest request);
    Chat completeDialog(Long id);
    List<Chat> getChatsByClientId(Long clientId);
    List<ChatHistoryResponse> getChatHistoryByClientId(Long clientId);
    List<Chat> getChatsBySupportId(Long supportId);
    void archiveChat(Long chatId);
    void rateChat(Long chatId, int rate);
    List<Chat> getAllChats();
    List<ChatHistoryResponse> getChats(boolean isButchery, String type);
    List<SupportChatResponse> filterChatsBySupportId(Long id, List<String> filter, Long startDate, Long endDate);
    List<ChatHistoryResponse> getChatsByButcheryId(Long id);


}
