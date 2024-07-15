package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kz.wave.hiba.Enum.ChatMessageType;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * This class represents a chat message.
 */
@Entity
@Table(name = "chat_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {

    /**
     * The unique identifier of the chat message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The identifier of the chat to which this message belongs.
     */
    @Column(name = "chat_id")
    private Long chat;

    /**
     * The content of the message.
     */
    @Column(name = "content")
    private String content;

    /**
     * The timestamp when the message was created.
     */
    @Column(name = "timestamp")
    private Date timestamp;

    /**
     * The type of sender (e.g., USER, SUPPORT).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type")
    private SenderType senderType;

    /**
     * The status of the message (e.g., SENT, RECEIVED, DELIVERED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus messageStatus = MessageStatus.SEND;

    /**
     * The type of the message (e.g., MESSAGE, IMAGE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private ChatMessageType chatMessageType = ChatMessageType.MESSAGE;

}
