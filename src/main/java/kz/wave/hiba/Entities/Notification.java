package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.NotificationCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * The class represents a notification.
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    /**
     * The unique identifier of the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The message content of the notification.
     */
    @Column(name = "message")
    private String message;

    /**
     * The time when the notification was created.
     */
    @Column(name = "time")
    private Instant time;

    /**
     * The category of the notification.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_category")
    private NotificationCategory notificationCategory;

    /**
     * Indicates whether the notification has been read.
     */
    @Column(name = "is_readed")
    private boolean isRead = false;

    /**
     * The user associated with the notification.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
