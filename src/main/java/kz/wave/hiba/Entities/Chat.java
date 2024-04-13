package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "support_id")
    private Long supportId;

    @Column(name = "archive")
    private boolean archive;

    @Column(name = "rate")
    private int rate;

}
