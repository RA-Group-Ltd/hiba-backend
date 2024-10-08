package kz.wave.hiba.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityCreateDTO {

    private Long id;
    private String name;
    private Long regionId;
    private Long countryId;

}
