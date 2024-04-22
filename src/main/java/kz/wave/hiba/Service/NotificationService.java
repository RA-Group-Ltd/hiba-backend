package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Enum.NotificationCategory;

public interface NotificationService {

    void sendNotificationToUser(Long id, NotificationCategory notificationCategory);

}
