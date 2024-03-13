package kz.wave.hiba.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryCreateDTO {

    private Long id;
    private String name;
    private Float latitude;
    private Float longitude;
    private String address;
    private Long cityId;

}
