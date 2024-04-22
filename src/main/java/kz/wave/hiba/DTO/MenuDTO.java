package kz.wave.hiba.DTO;

import kz.wave.hiba.Entities.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {

    private Menu menu;
    private int count;

}
