package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Notification;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.NotificationCategory;
import kz.wave.hiba.Repository.NotificationRepository;
import kz.wave.hiba.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendNotificationToUser(Order order) {
        User user = order.getUser();
        String message = "Ваш заказ передан в службу доставки " + "Ваш заказ будет доставлен " +  +  ". Курьер с вами заранее свяжется";

        Notification notification = new Notification();
        notification.setTime(Instant.now());
        notification.setMessage(message);
        notification.setUser(user);
//        notification.setNotificationCategory(NotificationCategory.);

        notificationRepository.save(notification);
    }
}
