package kz.wave.hiba.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private String meatType;
    private String phone;
    private String owner;
    private String regNumber;
    private String email;
    private String password;
    private List<MultipartFile> documents;

}
