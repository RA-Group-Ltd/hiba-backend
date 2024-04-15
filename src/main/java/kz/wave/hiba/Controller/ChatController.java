package kz.wave.hiba.Controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Entities.ChatNotification;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.SenderType;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ChatMessageService;
import kz.wave.hiba.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/chats")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, HttpServletRequest request) {

        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        // Получаем чат для данного сообщения
        Chat chat = chatMessage.getChat();
        Long recipientId;
        Long senderId;

        // Определяем идентификаторы отправителя и получателя в зависимости от типа отправителя
        if (chatMessage.getRecipientType() == SenderType.CLIENT) {
            senderId = chat.getClientId();
            recipientId = chat.getSupportId();
        } else {
            senderId = chat.getSupportId();
            recipientId = chat.getClientId();
        }

        // Сохранение сообщения в базу данных
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        String senderName = user.getName();

        // Отправка уведомления получателю
        messagingTemplate.convertAndSendToUser(
                String.valueOf(recipientId), "/queue/messages",
                new ChatNotification(
                        savedMessage.getId(),
                        senderId,
                        senderName)); // Предположим, что вы хотите отправить имя отправителя
    }

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestParam("clientId") Long clientId, @RequestParam("supportId") Long supportId) {
        Chat chat = chatService.createChat(clientId, supportId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }

    @GetMapping("/client/{clientId}/support/{supportId}")
    public ResponseEntity<Chat> getChatByClientAndSupport(@PathVariable Long clientId, @PathVariable Long supportId) {
        Optional<Chat> chat = chatService.getChatByClientAndSupport(clientId, supportId);
        return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public List<Chat> getChatsByClientId(@PathVariable Long clientId) {
        return chatService.getChatsByClientId(clientId);
    }

    @GetMapping("/support/{supportId}")
    public List<Chat> getChatsBySupportId(@PathVariable Long supportId) {
        return chatService.getChatsBySupportId(supportId);
    }

    @PutMapping("/archive/{chatId}")
    public ResponseEntity<?> archiveChat(@PathVariable Long chatId) {
        chatService.archiveChat(chatId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rate/{chatId}")
    public ResponseEntity<?> rateChat(@PathVariable Long chatId, @RequestParam("rate") int rate) {
        chatService.rateChat(chatId, rate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<Chat> getAllChats() {
        return chatService.getAllChats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Optional<Chat> chat = chatService.getChatById(id);
        return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
