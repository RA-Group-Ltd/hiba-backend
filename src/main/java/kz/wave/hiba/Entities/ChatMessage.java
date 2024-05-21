package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @ManyToOne
    @Column(name = "chat_id")
    private Long chat;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type")
    private SenderType senderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus messageStatus = MessageStatus.SEND;

}
