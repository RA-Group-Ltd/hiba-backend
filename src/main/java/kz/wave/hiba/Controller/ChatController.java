package kz.wave.hiba.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Enum.ChatMessageType;
import kz.wave.hiba.Enum.SenderType;
import kz.wave.hiba.Repository.ButcherRepository;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Response.ChatHistoryResponse;
import kz.wave.hiba.Response.ChatResponse;
import kz.wave.hiba.Response.ChatSupportNotification;
import kz.wave.hiba.Service.NotificationService;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ButcheryRepository butcheryRepository;

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/chat")
    public void processMessage(@Payload byte[] payload, Principal principal) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

            chatMessage.setTimestamp(new Date());



            // Получаем существующий чат по chatId, который был передан клиентом
            Chat chat = chatService.getChatById(chatMessage.getChat())
                    .orElseThrow(() -> new RuntimeException("Chat not found"));

            // Устанавливаем чат для сообщения
            chatMessage.setChat(chat.getId());

            if (chatMessage.getChatMessageType() == ChatMessageType.MESSAGE){
                ChatMessage savedMsg = chatMessageService.save(chatMessage);

                // Отправляем сообщение получателю
                messagingTemplate.convertAndSend( "/queue/chat/"+chat.getId(),
                    savedMsg
                );

                if(savedMsg.getSenderType() == SenderType.CLIENT){
                    ChatSupportNotification chatSupportNotification = new ChatSupportNotification();
                    chatSupportNotification.setContent(savedMsg.getContent());
                    chatSupportNotification.setIsButchery(chat.getIsButchery());
                    String chatType = chat.isArchive() ? "archive" : chat.getSupportId() == null ? "new" : "active";
                    chatSupportNotification.setChatType(chatType);
                    chatSupportNotification.setChat(chat.getId());

                    messagingTemplate.convertAndSend("/queue/notification",
                        chatSupportNotification
                    );
                } else {

                    notificationService.sendChatNotificationToUser(chat, savedMsg);

                }
            } else if (chatMessage.getChatMessageType() == ChatMessageType.END_DIALOG) {
                Chat chat1 = chatService.completeDialog(chat.getId());
                if (chat1 == null){
                    throw new RuntimeException();
                }
                messagingTemplate.convertAndSend( "/queue/chat/"+chat.getId(),
                    chatMessage
                );
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    @PostMapping("/create/butchery")
    public ResponseEntity<?> createButcheryChat(HttpServletRequest request){
        try {
            ChatHistoryResponse chatHistoryResponse = chatService.createButcheryChat(request);
            return new ResponseEntity<>(chatHistoryResponse, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestParam(value = "orderId", required = false) Long orderId, HttpServletRequest request) {
        try {
            ChatHistoryResponse chatHistoryResponse = chatService.createChat(orderId, request);
            return new ResponseEntity<>(chatHistoryResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrdersForChat(HttpServletRequest request){
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(username);

            List<Order> orders = orderRepository.findOrdersByUserIdAndNotInChats(user.getId());

            return new ResponseEntity<>(orders, HttpStatus.OK);

        }catch (Exception e){
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


    @GetMapping("/get")
    public List<ChatHistoryResponse> getChats(@RequestParam("isButchery") boolean isButchery, @RequestParam("type") String type) {
        try {
            return chatService.getChats(isButchery, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/butchery")
    public List<ChatHistoryResponse> getChatsByButchery(HttpServletRequest request){

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

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getChatsHistory(HttpServletRequest request){
        try{
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);
            User user = userRepository.findByUsername(username);

            if(user == null){
                return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
            }

            List<ChatHistoryResponse> chats = chatService.getChatHistoryByClientId(user.getId());
            return new ResponseEntity<>(chats, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
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
            User support = userRepository.findUserById(chat.getSupportId());

            if(chat.getIsButchery()){
                Optional<Butchery> butcheryOptional = butcheryRepository.findById(chat.getClientId());
                if(butcheryOptional.isPresent()){
                    Butchery butchery = butcheryOptional.get();
                    return new ChatResponse(chat, messages, null, support, butchery);
                }
            }else{
                User client = userRepository.findUserById(chat.getClientId());
                return new ChatResponse(chat, messages, client, support, null);
            }
        }
        return null;
    }

}
