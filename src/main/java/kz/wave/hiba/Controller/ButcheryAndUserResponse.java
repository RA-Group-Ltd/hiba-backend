package kz.wave.hiba.Controller;

import kz.wave.hiba.DTO.ButcheryCategoryCreateDTO;
import kz.wave.hiba.DTO.ButcheryCreateDTO;
import kz.wave.hiba.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryAndUserResponse {

    private ButcheryCreateDTO butcheryCreateDTO;
    private UserDTO userDTO;

    public ButcheryCreateDTO getButchery() {
        return butcheryCreateDTO;
    }

    public void setButchery(ButcheryCreateDTO butchery) {
        this.butcheryCreateDTO = butchery;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(UserDTO user) {
        this.userDTO = user;
    }

}
