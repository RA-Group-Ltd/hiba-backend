package kz.wave.hiba.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class represents the composite key for the UserRole entity.
 */
@Embeddable
public class UserRoleId implements Serializable {

    /**
     * The user identifier part of the composite key.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * The role identifier part of the composite key.
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * Default constructor for the UserRoleId class.
     */
    public UserRoleId() {

    }

    /**
     * Constructs a new UserRoleId with the specified user ID and role ID.
     *
     * @param userId the user ID part of the composite key
     * @param roleId the role ID part of the composite key
     */
    public UserRoleId(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    /**
     * Returns the user ID part of the composite key.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID part of the composite key.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the role ID part of the composite key.
     *
     * @return the role ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID part of the composite key.
     *
     * @param roleId the role ID to set
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Compares this UserRoleId to another object for equality.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    /**
     * Returns the hash code of this UserRoleId.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
