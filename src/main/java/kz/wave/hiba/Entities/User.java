package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents a user.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    /**
     * The unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The name of the user.
     */
    @Column(name = "name")
    private String name;

    /**
     * The phone number of the user, used as the username for authentication.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * The password of the user.
     */
    @Column(name = "password")
    private String password;

    /**
     * The avatar of the user.
     */
    @Column(name = "avatar")
    private byte[] avatar;

    /**
     * The date and time when the user was created.
     */
    @Column(name = "createdAt")
    private Instant createdAt;

    /**
     * The Telegram chat ID of the user.
     */
    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    /**
     * Indicates whether the user is confirmed.
     */
    @Column(name = "confirmed")
    private boolean confirmed = false;

    /**
     * The email of the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * The roles associated with the user.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the phone number used as the username for authentication.
     *
     * @return the phone number.
     */
    @Override
    public String getUsername() {
        return phone;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the user's account is non-expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is non-locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true if the user's credentials are non-expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
