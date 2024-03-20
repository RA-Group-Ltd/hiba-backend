package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Entities.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryCategoryResponse {

    private Long id;
    private Category category;
    private List<Menu> menuItems;

}
