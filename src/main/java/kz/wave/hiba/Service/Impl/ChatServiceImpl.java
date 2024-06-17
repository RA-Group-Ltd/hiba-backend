package kz.wave.hiba.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.ChatStatus;
import kz.wave.hiba.Repository.ButcherRepository;
import kz.wave.hiba.Repository.ChatRepository;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Response.ChatHistoryResponse;
import kz.wave.hiba.Response.SupportChatResponse;
import kz.wave.hiba.Service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final OrderRepository orderRepository;
    private final ButcherRepository butcherRepository;

    @Override
    public ChatHistoryResponse createChat(Long orderId, HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        Chat chat = new Chat();
        chat.setClientId(user.getId());

        if (orderId != null){
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                chat.setOrder(order);
            }
        }


        chat.setArchive(false);
        chat.setRate(0);
        chat.setChatStatus(ChatStatus.ACTIVE);
        Chat savedChat = chatRepository.save(chat);
        User nullUser = null;
        return new ChatHistoryResponse(savedChat, nullUser);
    }

    @Override
    public ChatHistoryResponse createButcheryChat(HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        Butchery butchery = butcherRepository.findButcheryByUserId(user.getId());

        Chat chat = new Chat();
        chat.setClientId(butchery.getId());
        chat.setIsButchery(true);

        chat.setArchive(false);
        chat.setRate(0);
        chat.setChatStatus(ChatStatus.ACTIVE);
        Chat savedChat = chatRepository.save(chat);
        User nullUser = null;
        return new ChatHistoryResponse(savedChat, nullUser);
    }



    @Override
    public Chat startDialog(Long chatId, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByPhone(currentUser);

        Optional<Chat> chatOptional = chatRepository.findById(chatId);

        if (chatOptional.isEmpty()) {
            return null;
        }

        Chat chat = chatOptional.get();
        chat.setSupportId(user.getId());

        return chatRepository.save(chat);
    }

    @Override
    public Chat startDialog(Long chatId) {
        return null;
    }

    @Override
    public ResponseEntity<?> completeDialog(Long chatId, HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(token);
        User user = userRepository.findByPhone(currentUser);

        Optional<Chat> chatOptional = chatRepository.findById(chatId);

        if (chatOptional.isEmpty()) {
            return null;
        }

        Chat chat = chatOptional.get();

        if (!Objects.equals(chat.getSupportId(), user.getId())) {
            return null;
        }

        chat.setArchive(true);
        chat.setChatStatus(ChatStatus.ARCHIVE);

        return new ResponseEntity<>(chatRepository.save(chat), HttpStatus.CREATED);
    }

    @Override
    public Chat completeDialog(Long id) {
        Optional<Chat> chatOptional = chatRepository.findById(id);

        if (chatOptional.isEmpty()) {
            return null;
        }

        Chat chat = chatOptional.get();

        chat.setArchive(true);
        chat.setChatStatus(ChatStatus.ARCHIVE);

        return chatRepository.save(chat);
    }

//    @Override
//    public Optional<Chat> getChatByClientAndSupport(Long clientId, Long supportId) {
//        return chatRepository.findByClientIdAndSupportId(clientId, supportId);
//    }

    @Override
    public List<Chat> getChatsByClientId(Long clientId) {
        return chatRepository.findByClientId(clientId);
    }

    @Override
    public List<ChatHistoryResponse> getChatHistoryByClientId(Long clientId) {
        return chatRepository.findChatHistoryByClientId(clientId);
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


    /*@Override
    public Chat createChatIfNotExist(Long clientId, Long supportId) {
        Optional<Chat> existingChat = chatRepository.findByClientIdAndSupportId(clientId, supportId);
        if (existingChat.isPresent()) {
            return existingChat.get();
        } else {
            Chat newChat = Chat.builder()
                    .clientId(clientId)
                    .supportId(supportId)
                    .archive(false)
                    .rate(0)
                    .chatStatus(ChatStatus.ACTIVE)  // Убедитесь, что ChatStatus.ACTIVE существует
                    .build();
            return chatRepository.save(newChat);
        }
    }*/

    @Override
    public List<ChatHistoryResponse> getChats(boolean isButchery, String type) {
        if(isButchery){
            return switch (type) {
                case "archive" -> chatRepository.findChatsByArchiveIsTrueAndIsButchery();
                case "active" -> chatRepository.findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButchery();
                default -> chatRepository.findChatsBySupportIdIsNullAndIsButcheryAndArchiveIsFalse();
            };
        }
        else{
            return switch (type) {
                case "archive" -> chatRepository.findChatsByArchiveIsTrueAndIsButcheryFalse();
                case "active" -> chatRepository.findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButcheryFalse();
                default -> chatRepository.findChatsBySupportIdIsNullAndIsButcheryFalseAndArchiveIsFalse();
            };
        }


    }

    @Override
    public List<SupportChatResponse> filterChatsBySupportId(Long id, List<String> filter, Long startDate, Long endDate) {
        Instant stDate = null;
        Instant edDate = null;

        try {
            if (startDate != null) {
                stDate = Instant.ofEpochMilli(startDate);
            }
            if (endDate != null) {
                edDate = Instant.ofEpochMilli(endDate);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        return chatRepository.findChatsBySupportId(id, filter, stDate, edDate);
    }

    @Override
    public List<ChatHistoryResponse> getChatsByButcheryId(Long id) {
        return chatRepository.findChatsByButcheryId(id);
    }
/*@Override
    public void addUserToChat(Chat chat, Long supportId) {
        // Получаем текущий список пользователей чата
        List<Long> users = chat.getUsers();

        // Проверяем, не добавлен ли уже supportId
        if (!users.contains(supportId)) {
            // Добавляем supportId в список
            users.add(supportId);
        }

        // Сохраняем обновленный список пользователей в чате
        chat.setUsers(users);

        // Сохранение обновленного чата в базу данных
        chatRepository.save(chat);
    }*/

    @Override
    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }
}
