package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The class represents a verification code used for user verification.
 */
@Entity
@Table(name = "verification_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode extends BaseEntity {

    /**
     * The verification token.
     */
    @Column(name = "token")
    private String token;

    /**
     * The user associated with this verification code.
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    /**
     * The expiration date of the verification code.
     */
    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expirationDate;

    /**
     * Constructs a new VerificationCode with the specified token and user.
     *
     * @param token the verification token
     * @param user  the user associated with this verification code
     */
    public VerificationCode(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
