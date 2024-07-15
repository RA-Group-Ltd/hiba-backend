package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a chat notification.
 */
@Entity
@Table(name = "chat_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {

    /**
     * The unique identifier of the chat notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The identifier of the associated message.
     */
    @Column(name = "message_id")
    private Long messageId;

    /**
     * The identifier of the sender.
     */
    @Column(name = "sender_id")
    private Long senderId;

    /**
     * The name of the sender.
     */
    @Column(name = "sender_name")
    private String senderName;

    /**
     * Constructs a new ChatNotification with the specified message ID, sender ID, and sender name.
     *
     * @param messageId the identifier of the associated message
     * @param senderId the identifier of the sender
     * @param senderName the name of the sender
     */
    public ChatNotification(Long messageId, Long senderId, String senderName) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
    }
}
