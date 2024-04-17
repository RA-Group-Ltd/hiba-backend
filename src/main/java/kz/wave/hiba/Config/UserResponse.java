package kz.wave.hiba.Config;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.UserRole;
import lombok.Data;

@Data
public class UserResponse {

    private User user;
    private String token;
    private UserRole userRole;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserResponse(String token, User user, UserRole userRole) {
        this.token = token;
        this.user = user;
        this.userRole = userRole;
    }

}
