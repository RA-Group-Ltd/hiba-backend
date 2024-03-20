package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Entities.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryResponse {
    private Long id;
    private String name;
    private float latitude;
    private float longitude;
    private String address;
    private City city;
    private List<ButcheryCategoryResponse> categories;
}
