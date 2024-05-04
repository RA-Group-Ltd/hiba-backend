package kz.wave.hiba.Service.Impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Notification;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.NotificationCategory;
import kz.wave.hiba.Repository.NotificationRepository;
import kz.wave.hiba.Repository.OrderRepository;
import kz.wave.hiba.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;

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

        String token = "fVZIK6o0RCaOFHuCWM3jfF:APA91bF0k-fvhi_GtgczDNhHN2v6bO_fbI6inCerjfhZh91Buo2vN7eFw0Q0euRXPvyXxRhz80Dv4SKixdj9iivjDfJD9d5xDCClFjlwV4mu1LfhzBTk28UJvlyE4KVd25qqcOED3pbO";

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
}
