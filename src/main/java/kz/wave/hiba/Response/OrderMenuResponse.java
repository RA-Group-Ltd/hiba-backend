package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.ButcheryCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMenuResponse {
    private Long id;
    private String name;
    private String description;
    private int weight;
    private Boolean isWholeAnimal;
    private int price;
    private ButcheryCategory butcheryCategory;
    private byte[] image;
    private int quantity;
}
