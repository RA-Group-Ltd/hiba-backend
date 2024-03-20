package kz.wave.hiba.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateDTO {

    private Long id;
    private String building;
    private String name;
    private String address;
    private String floor;
    private String entrance;
    private String apartment;
    private Long city_id;

}
