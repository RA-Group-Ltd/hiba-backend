package kz.wave.hiba.DTO;

import kz.wave.hiba.Entities.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionUpdateDTO {
    private Long id;
    private String name;
    private Country country;
}
