package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @OneToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "order_id")
    private Order order;

    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expirationDate;

    public ConfirmationCode(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
