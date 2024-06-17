package kz.wave.hiba.DTO;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelUserDTO {

    private String name;
    private String newPassword;
    private String reTypeNewPassword;
    private MultipartFile avatar;

}
