package kz.wave.hiba.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateDTO {

    private Long id;
    private String name;
    private int weight;
    private Boolean isWholeAnimal;
    private int price;
    private Long butcheryCategoryId;
    private Long categoryId;
    private MultipartFile image;
    private String description;

}
