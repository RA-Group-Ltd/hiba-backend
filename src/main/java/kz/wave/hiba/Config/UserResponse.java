package kz.wave.hiba.Config;

import kz.wave.hiba.Entities.User;
import lombok.Data;

@Data
public class UserResponse {

    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

}
