package kz.wave.hiba.Service.Impl;

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

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final OrderRepository orderRepository;

    @Override
    public void sendNotificationToUser(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        Order order = orderOptional.get();
        User user = order.getUser();
        String message = "Ваш заказ передан в службу доставки " + "Ваш заказ будет доставлен " + Instant.now() + ". Курьер с вами заранее свяжется";

        Notification notification = new Notification();
        notification.setTime(Instant.now());
        notification.setMessage(message);
        notification.setUser(user);
        notification.setNotificationCategory(NotificationCategory.REMINDERS);

        notificationRepository.save(notification);
    }
}
