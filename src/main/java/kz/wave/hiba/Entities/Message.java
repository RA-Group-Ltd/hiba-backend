package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * The class represents a message.
 */
@Entity
@Table(name = "message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    /**
     * The unique identifier of the message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The chat to which this message belongs.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "chat")
    private Chat chat;

    /**
     * The user who sent the message.
     */
    @OneToOne
    @JoinColumn(name = "sender")
    private User sender;

    /**
     * The content of the message.
     */
    @Column(name = "content")
    private String content;

    /**
     * The images associated with the message.
     */
    @Column(name = "images")
    private byte[] images;

    /**
     * The timestamp when the message was created.
     */
    @Column(name = "created_at")
    private Instant createAt;

}
