package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.ChatStatus;
import kz.wave.hiba.Enum.OrderStatus;
import lombok.*;

import javax.annotation.Nullable;

@Entity
@Table(name = "chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "is_butchery")
    private Boolean isButchery;

    @Builder.Default
    @Column(name = "support_id")
    private Long supportId = null;

    @Column(name = "archive")
    private boolean archive;

    @Column(name = "rate")
    private int rate;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_status")
    private ChatStatus chatStatus;

    @Nullable
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
