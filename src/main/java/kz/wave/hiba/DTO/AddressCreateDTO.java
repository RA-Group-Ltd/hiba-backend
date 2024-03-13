package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateDTO {

    private String building_name;
    private String entrance;
    private String floor;
    private String apartment;
    @JsonIgnore
    private Long user_id;
    private Long city_id;

}
