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
public class ButcheryCategoryCreateDTO {

    @JsonIgnore
    private Long id;
    private Long butcheryId;
    private Long categoryId;


}
