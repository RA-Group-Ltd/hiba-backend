package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Order;

public interface NotificationService {

    void sendNotificationToUser(Long id);

}
