package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_name")
    private String senderName;

    public ChatNotification(Long messageId, Long senderId, String senderName) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
    }
}
