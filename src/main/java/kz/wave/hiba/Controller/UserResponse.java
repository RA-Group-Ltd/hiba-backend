package kz.wave.hiba.Controller;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Entities.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String token;
    private User user;
    private String userRole;

    public UserResponse(String token, User users, UserRole userRole) {
        this.token = token;
        this.user = users;
        this.userRole = userRole.getRole().getName();
    }

}
