package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Notification notification;
    private String message;
    private Object notificationData;

}
