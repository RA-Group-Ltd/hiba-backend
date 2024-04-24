package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.NotificationCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "time")
    private Instant time;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_category")
    private NotificationCategory notificationCategory;

    @Column(name = "is_readed")
    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
