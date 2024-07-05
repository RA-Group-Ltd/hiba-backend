package kz.wave.hiba.Service.Impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.*;
import kz.wave.hiba.Enum.NotificationCategory;
import kz.wave.hiba.Repository.NotificationRepository;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Repository.PromotionRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;

    private final static String token = "fVZIK6o0RCaOFHuCWM3jfF:APA91bF0k-fvhi_GtgczDNhHN2v6bO_fbI6inCerjfhZh91Buo2vN7eFw0Q0euRXPvyXxRhz80Dv4SKixdj9iivjDfJD9d5xDCClFjlwV4mu1LfhzBTk28UJvlyE4KVd25qqcOED3pbO";

    @Override
    public void sendNotificationToUser(Long id, NotificationCategory notificationCategory) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        Order order = orderOptional.get();
        User user = order.getUser();
        Instant deliveryTime = order.getDeliveryDate();
        String message = "Ваш заказ передан в службу доставки " + "Ваш заказ будет доставлен " + deliveryTime + ". Курьер с вами заранее свяжется";

        Notification notification = new Notification();
        notification.setTime(deliveryTime);
        notification.setMessage(message);
        notification.setUser(user);
        notification.setNotificationCategory(notificationCategory);
//        notification.setRead();

        notificationRepository.save(notification);

        Message fcmMessage = Message.builder().setNotification(
                com.google.firebase.messaging.Notification.builder()
                        .setTitle(notificationCategory.toString())
                        .setBody(message)
                        .build()
                )
                .setToken(token)
                .build();

        try {
            FirebaseMessaging.getInstance().send(fcmMessage);
        } catch (FirebaseMessagingException firebaseMessagingException) {
            firebaseMessagingException.printStackTrace();
        }
    }

    @Override
    public void sendNotificationPromotion(String title) {
        Promotion promotion = promotionRepository.findPromotionByTitle(title);
        List<User> users = userRepository.findAll();


        for (User user : users) {
            Notification notification = new Notification();
            notification.setTime(Instant.now());
            notification.setNotificationCategory(NotificationCategory.PROMOTIONS);
            notification.setMessage(promotion.getTitle());
            notification.setUser(user);

            notificationRepository.save(notification);

            Message fcmMessage = Message.builder().setNotification(
                            com.google.firebase.messaging.Notification.builder()
                                    .setTitle(NotificationCategory.PROMOTIONS.toString())
                                    .setBody(promotion.getTitle())
                                    .build()
                    )
                    .setToken(token)
                    .build();

            try {

                FirebaseMessaging.getInstance().send(fcmMessage);

            } catch (FirebaseMessagingException firebaseMessagingException) {

                firebaseMessagingException.printStackTrace();

            }
        }
    }

    @Override
    public void sendChatNotificationToUser(Chat chat, ChatMessage savedMsg) {
        Notification notification = new Notification();
        notification.setUser(userRepository.findUserById(chat.getClientId()));
        notification.setTime(Instant.now());
        notification.setMessage(savedMsg.getContent());
        notification.setNotificationCategory(NotificationCategory.USER_MESSAGES);

        notificationRepository.save(notification);

        Message fcmMessage = Message.builder().setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(NotificationCategory.USER_MESSAGES.toString())
                                .setBody(savedMsg.getContent())
                                .build()
                )
                .setToken(token)
                .build();

        try {

            FirebaseMessaging.getInstance().send(fcmMessage);

        } catch (FirebaseMessagingException firebaseMessagingException) {

            firebaseMessagingException.printStackTrace();

        }
    }
}
