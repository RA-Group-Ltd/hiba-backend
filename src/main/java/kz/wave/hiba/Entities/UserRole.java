package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRole{

    @EmbeddedId
    private UserRoleId id;

    @JsonIgnore
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public UserRole() {

    }

    public UserRole(UserRoleId id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }
}
