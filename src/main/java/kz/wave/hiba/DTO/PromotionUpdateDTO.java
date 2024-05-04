package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.wave.hiba.Enum.Audience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionUpdateDTO {

    @JsonIgnore
    private Long id;
    private String title;
    private String description;
    private Audience audience;
    private MultipartFile image;

}
