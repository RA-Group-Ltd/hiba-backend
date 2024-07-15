package kz.wave.hiba.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * The class represents a role granted to a user.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    /**
     * The name of the role.
     */
    @Column(name = "name")
    private String name;

    /**
     * Returns the authority granted to the user.
     *
     * @return the name of the role.
     */
    @Override
    public String getAuthority() {
        return name;
    }
}
