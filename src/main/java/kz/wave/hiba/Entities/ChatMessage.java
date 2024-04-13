package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.MessageStatus;
import kz.wave.hiba.Enum.SenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat")
    private Chat chat;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

}
