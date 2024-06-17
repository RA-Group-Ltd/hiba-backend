package kz.wave.hiba.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ModelUserDTO {
    @JsonIgnore
    private Long id;
    private String name;
    private String phone;
    private String email;
    private MultipartFile avatar;

}
