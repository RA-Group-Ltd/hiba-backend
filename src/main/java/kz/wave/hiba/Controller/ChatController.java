package kz.wave.hiba.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Repository.ButcherRepository;
import kz.wave.hiba.Response.ChatResponse;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ChatMessageService;
import kz.wave.hiba.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/chats")
public class ChatController {

    @Autowired
    private ObjectMapper objectMapper;

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

    @Autowired
    private ButcherRepository butcherRepository;

    @MessageMapping("/chat")
    public void processMessage(@Payload byte[] payload, Principal principal) {
        try {
            System.out.println(Arrays.toString(payload));

            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
            System.out.println(chatMessage);

            chatMessage.setTimestamp(new Date());

            // Получаем существующий чат по chatId, который был передан клиентом
            Chat chat = chatService.getChatById(chatMessage.getChat())
                    .orElseThrow(() -> new RuntimeException("Chat not found"));

            // Устанавливаем чат для сообщения
            chatMessage.setChat(chat.getId());

            ChatMessage savedMsg = chatMessageService.save(chatMessage);

            System.out.println(chat.getId());


            // Отправляем сообщение получателю
            messagingTemplate.convertAndSend( "/queue/chat/"+chat.getId(),
                    chatMessage
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /*@MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getChat().getSupportId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getChat().getClientId(),
                        savedMsg.getChat().getSupportId(),
                        savedMsg.getContent()
                )
        );
    }*/

    /*@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload byte[] messageBytes) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ChatMessage chatMessage = mapper.readValue(messageBytes, ChatMessage.class);
        chatMessage.setTimestamp(new Date());  // Установите текущую дату и время
        chatMessageService.save(chatMessage);  // Сохраните сообщение
        return chatMessage;
    }

    @MessageMapping("/chat.sendNewMessage") // Изменили путь
    @SendTo("/topic/public")
    public ChatMessage processMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String userToken = headerAccessor.getSessionAttributes().get("token").toString();
        User user = jwtUtils.validateAndGetUserFromToken(userToken);

        if (user.getRoles().contains("ROLE_SUPPORT")) {
            chatMessage.setSender(SenderType.SUPPORT);
        } else if (user.getRoles().contains("ROLE_USER")) {
            chatMessage.setSender(SenderType.CLIENT);
        } else {
            chatMessage.setSender(SenderType.BOT);
        }

        chatMessage.setTimestamp(new Date());  // Установите текущую дату и время
        Chat chat = chatService.getChatById(chatMessage.getChat().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found or not active")
        );

        chatMessage.setChat(chat);
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/messages/" + chat.getId(), savedMessage);
        return savedMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        chatService.addUserToChat(chatMessage.getChat(), chatMessage.getSender());
        return chatMessage;
    }*/

/*    @MessageMapping("/chat.sendMessage")
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



    /*@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
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
    }*/


    @PostMapping("/create/butchery")
    public ResponseEntity<?> createButcheryChat(HttpServletRequest request){
        try {
            Chat chat = chatService.createButcheryChat(request);
            return new ResponseEntity<>(chat, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<?> startDialog(@PathVariable Long chatId, HttpServletRequest request) {
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
    public ResponseEntity<?> completeDialog(@PathVariable Long chatId, HttpServletRequest request) {
        try {
            return chatService.completeDialog(chatId, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/client/{clientId}/support/{supportId}")
    public ResponseEntity<?> getChatByClientAndSupport(@PathVariable Long clientId, @PathVariable Long supportId) {
        try {
            Optional<Chat> chat = chatService.getChatByClientAndSupport(clientId, supportId);
            return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }*/

    /*@GetMapping("/client/{clientId}/support/{supportId}")
    public ResponseEntity<?> getChatByClientAndSupport(@PathVariable Long clientId, @PathVariable Long supportId) {
        System.out.println("Received request for clientId: " + clientId + " and supportId: " + supportId);
        try {
            Chat chat = chatService.createChatIfNotExist(clientId, supportId);
            System.out.println("Chat found or created: " + chat);

            List<ChatMessage> messages = chatMessageService.findMessagesByChatId(chat.getId());
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }*/

    @GetMapping("/get")
    public List<Chat> getChats(@RequestParam("isButchery") boolean isButchery, @RequestParam("type") String type) {
        try {
            return chatService.getChats(isButchery, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/butchery")
    public List<Chat> getChatsByButchery(HttpServletRequest request){
        String token = jwtUtils.getTokenFromRequest(request);
        String username = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);

        Butchery butchery = butcherRepository.findButcheryByUserId(user.getId());

        return chatService.getChatsByButcheryId(butchery.getId());
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

    @GetMapping("/byId/{id}")
    public ChatResponse getChatById(@PathVariable Long id) {
        Optional<Chat> chatOpt = chatService.getChatById(id);
        if(chatOpt.isPresent()){
            Chat chat = chatOpt.get();
            List<ChatMessage> messages =  chatMessageService.getMessagesByChat(chat.getId());

            return new ChatResponse(chat, messages);
        }
        return null;
    }

}
