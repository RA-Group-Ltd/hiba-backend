package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The class represents the association between a user and a role.
 */
@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRole {

    /**
     * The composite key for the user role association.
     */
    @EmbeddedId
    private UserRoleId id;

    /**
     * The user associated with this role.
     */
    @JsonIgnore
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * The role associated with this user.
     */
    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    /**
     * Default constructor for the UserRole class.
     */
    public UserRole() {

    }

    /**
     * Constructs a new UserRole with the specified ID, user, and role.
     *
     * @param id   the composite key for the user role association
     * @param user the user associated with this role
     * @param role the role associated with this user
     */
    public UserRole(UserRoleId id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }
}
