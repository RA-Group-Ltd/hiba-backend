package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
import kz.wave.hiba.Enum.NotificationCategory;

public interface NotificationService {

    void sendNotificationToUser(Long id, NotificationCategory notificationCategory);
    void sendNotificationPromotion(String title);
    void sendChatNotificationToUser(Chat chat, ChatMessage chatMessage);

}
