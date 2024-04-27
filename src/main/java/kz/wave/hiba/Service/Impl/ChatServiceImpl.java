package kz.wave.hiba.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.ChatRepository;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final OrderRepository orderRepository;

    @Override
    public Chat createChat(Long supportId, Long orderId, HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        Chat chat = new Chat();
        chat.setClientId(user.getId());
        Optional<User> supportUserOptional = userRepository.findById(supportId);
        if (supportUserOptional.isEmpty()) {
            return null;
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return null;
        }

        Order order = orderOptional.get();

        User supportUser = supportUserOptional.get();
        chat.setSupportId(supportUser.getId());
        chat.setArchive(false);
        chat.setRate(0);
        chat.setOrder(order);
        return chatRepository.save(chat);
    }

    @Override
    public Optional<Chat> getChatByClientAndSupport(Long clientId, Long supportId) {
//        clientId = userRepository
//        supportId = userRepository
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
