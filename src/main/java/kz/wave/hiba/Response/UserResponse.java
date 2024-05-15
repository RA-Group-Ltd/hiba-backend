package kz.wave.hiba.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.UserRole;
import lombok.Data;

@Data
public class UserResponse {

    private User user;
    private String token;
    @JsonIgnore
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
