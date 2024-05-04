package kz.wave.hiba.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryUpdateDTO {

    private Long id;
    private String name;
    private Float latitude;
    private Float longitude;
    private String address;
    private Long cityId;
    private List<MultipartFile> documents;

}
