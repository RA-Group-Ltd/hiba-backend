package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourierCreateDTO {

    @JsonIgnore
    private Long id;
    private String name;
    private Long cityId;
    private String phone;
    private String email;
    @JsonIgnore
    private String password;

}
