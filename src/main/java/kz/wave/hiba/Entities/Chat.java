package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.ChatStatus;
import lombok.*;

import javax.annotation.Nullable;
import java.time.Instant;

/**
 * This class represents a chat.
 */
@Entity
@Table(name = "chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {

    /**
     * The unique identifier of the chat.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The identifier of the client participating in the chat.
     */
    @Column(name = "client_id")
    private Long clientId;

    /**
     * Indicates whether the chat is related to a butchery.
     */
    @Column(name = "is_butchery")
    private Boolean isButchery;

    /**
     * The identifier of the support person assigned to the chat.
     * By default, this field is null.
     */
    @Builder.Default
    @Column(name = "support_id")
    private Long supportId = null;

    /**
     * Indicates whether the chat is archived.
     */
    @Column(name = "archive")
    private boolean archive;

    /**
     * The rating given to the chat.
     */
    @Column(name = "rate")
    private int rate;

    /**
     * The timestamp when the chat was created.
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * The status of the chat.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chat_status")
    private ChatStatus chatStatus;

    /**
     * The order associated with this chat, if any.
     */
    @Nullable
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
