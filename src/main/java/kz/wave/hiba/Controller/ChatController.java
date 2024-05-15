package kz.wave.hiba.Controller;

import com.google.api.Http;
import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
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
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

/*    @MessageMapping("/message")
    @SendTo("topic/message")
    public String processMessage(@Payload String message) {
        System.out.println(message);
        return message;
String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        // Получаем чат для данного сообщения
        Chat chat = chatMessage.getChat();
//        Long recipientId;
        Long senderId;

        // Определяем идентификаторы отправителя и получателя в зависимости от типа отправителя
        if (chatMessage.getSender() == SenderType.CLIENT) {
            senderId = chat.getClientId();
//            recipientId = chat.getSupportId();
        } else {
            senderId = chat.getSupportId();
//            recipientId = chat.getClientId();
        }

        // Сохранение сообщения в базу данных
//        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        chatMessageService.save(chatMessage);
//        String senderName = user.getName();

        // Отправка уведомления получателю
        messagingTemplate.convertAndSendToUser(
                String.valueOf(recipientId), "/queue/messages",
                new ChatNotification(
                        savedMessage.getId(),
                        senderId,
                        senderName)); // Предположим, что вы хотите отправить имя отправителя

    }*/

@MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage processMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String userToken = headerAccessor.getSessionAttributes().get("token").toString();
        User user = jwtUtils.validateAndGetUserFromToken(userToken);

        // Определение SenderType на основе роли пользователя
        if (user.getRoles().equals("ROLE_SUPPORT")) {
            chatMessage.setSender(SenderType.SUPPORT);
        } else if (user.getRoles().equals("ROLE_USER")) {
            chatMessage.setSender(SenderType.CLIENT);
        } else {
            chatMessage.setSender(SenderType.BOT);  // Все остальные роли как BOT (или другая логика)
        }

        Chat chat = chatService.getChatById(chatMessage.getChat().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found or not active")
        );

        chatMessage.setChat(chat);
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/messages/" + chat.getId(), savedMessage);
        return savedMessage;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        try {
            Chat chat = chatService.createChat(orderId, request);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/startDialog/{chatId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_SUPPORT')")
    public ResponseEntity<?> startDialog(@RequestParam("chatId") Long chatId, HttpServletRequest request) {
        try {
            Chat chat = chatService.startDialog(chatId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(chat);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/completeDialog/{chatId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_SUPPORT')")
    public ResponseEntity<?> completeDialog(@RequestParam("chatId") Long chatId, HttpServletRequest request) {
        try {
            return chatService.completeDialog(chatId, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/client/{clientId}/support/{supportId}")
    public ResponseEntity<?> getChatByClientAndSupport(@PathVariable Long clientId, @PathVariable Long supportId) {
        try {
            Optional<Chat> chat = chatService.getChatByClientAndSupport(clientId, supportId);
            return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Optional<Chat> chat = chatService.getChatById(id);
        return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
