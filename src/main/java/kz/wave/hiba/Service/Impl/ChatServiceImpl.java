package kz.wave.hiba.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.Config.JwtUtils;
import kz.wave.hiba.Entities.*;
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

/**
 * Implementation of the {@link ChatService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final OrderRepository orderRepository;
    private final ButcherRepository butcherRepository;

    /**
     * Creates a chat for a given order.
     *
     * @param orderId the ID of the order
     * @param request the HTTP request
     * @return the created chat history response
     */
    @Override
    public ChatHistoryResponse createChat(Long orderId, HttpServletRequest request) {
        String userToken = jwtUtils.getTokenFromRequest(request);
        String currentUser = jwtUtils.getUsernameFromToken(userToken);
        User user = userRepository.findByPhone(currentUser);

        Chat chat = new Chat();
        chat.setClientId(user.getId());

        if (orderId != null) {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                chat.setOrder(order);
            }
        }

        chat.setCreatedAt(Instant.now());
        chat.setIsButchery(false);
        chat.setArchive(false);
        chat.setRate(0);
        chat.setChatStatus(ChatStatus.ACTIVE);
        Chat savedChat = chatRepository.save(chat);
        User nullUser = null;
        return new ChatHistoryResponse(savedChat, nullUser);
    }

    /**
     * Creates a chat for a butchery.
     *
     * @param request the HTTP request
     * @return the created butchery chat history response
     */
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

    /**
     * Starts a dialog for a given chat.
     *
     * @param chatId the ID of the chat
     * @param request the HTTP request
     * @return the updated chat
     */
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

    /**
     * Starts a dialog for a given chat.
     *
     * @param chatId the ID of the chat
     * @return the updated chat
     */
    @Override
    public Chat startDialog(Long chatId) {
        return null;
    }

    /**
     * Completes a dialog for a given chat.
     *
     * @param chatId the ID of the chat
     * @param request the HTTP request
     * @return the response entity with the updated chat
     */
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

    /**
     * Completes a dialog for a given chat.
     *
     * @param id the ID of the chat
     * @return the updated chat
     */
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

    /**
     * Retrieves chats by client ID.
     *
     * @param clientId the ID of the client
     * @return a list of chats
     */
    @Override
    public List<Chat> getChatsByClientId(Long clientId) {
        return chatRepository.findByClientId(clientId);
    }

    /**
     * Retrieves chat history by client ID.
     *
     * @param clientId the ID of the client
     * @return a list of chat history responses
     */
    @Override
    public List<ChatHistoryResponse> getChatHistoryByClientId(Long clientId) {
        return chatRepository.findChatHistoryByClientId(clientId);
    }

    /**
     * Retrieves chats by support ID.
     *
     * @param supportId the ID of the support
     * @return a list of chats
     */
    @Override
    public List<Chat> getChatsBySupportId(Long supportId) {
        return chatRepository.findBySupportId(supportId);
    }

    /**
     * Archives a chat by its ID.
     *
     * @param chatId the ID of the chat to be archived
     */
    @Override
    public void archiveChat(Long chatId) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        optionalChat.ifPresent(chat -> {
            chat.setArchive(true);
            chatRepository.save(chat);
        });
    }

    /**
     * Rates a chat.
     *
     * @param chatId the ID of the chat to be rated
     * @param rate the rating to be set
     */
    @Override
    public void rateChat(Long chatId, int rate) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        optionalChat.ifPresent(chat -> {
            chat.setRate(rate);
            chatRepository.save(chat);
        });
    }

    /**
     * Retrieves all chats.
     *
     * @return a list of all chats
     */
    @Override
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    /**
     * Retrieves chats based on butchery status and type.
     *
     * @param isButchery indicates if the chats are for butcheries
     * @param type the type of chats to retrieve (archive, active, etc.)
     * @return a list of chat history responses
     */
    @Override
    public List<ChatHistoryResponse> getChats(boolean isButchery, String type) {
        if (isButchery) {
            return switch (type) {
                case "archive" -> chatRepository.findChatsByArchiveIsTrueAndIsButchery();
                case "active" -> chatRepository.findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButchery();
                default -> chatRepository.findChatsBySupportIdIsNullAndIsButcheryAndArchiveIsFalse();
            };
        } else {
            return switch (type) {
                case "archive" -> chatRepository.findChatsByArchiveIsTrueAndIsButcheryFalse();
                case "active" -> chatRepository.findChatsByArchiveIsFalseAndSupportIdNotNullAndIsButcheryFalse();
                default -> chatRepository.findChatsBySupportIdIsNullAndIsButcheryFalseAndArchiveIsFalse();
            };
        }
    }

    /**
     * Filters chats by support ID, filter criteria, and date range.
     *
     * @param id the ID of the support
     * @param filter the filter criteria
     * @param startDate the start date of the filter range
     * @param endDate the end date of the filter range
     * @return a list of support chat responses
     */
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

    /**
     * Retrieves chats by butchery ID.
     *
     * @param id the ID of the butchery
     * @return a list of chat history responses
     */
    @Override
    public List<ChatHistoryResponse> getChatsByButcheryId(Long id) {
        return chatRepository.findChatsByButcheryId(id);
    }

    /**
     * Retrieves a chat by its ID.
     *
     * @param id the ID of the chat
     * @return an {@link Optional} containing the chat, if found
     */
    @Override
    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }
}
