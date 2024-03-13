package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteRegistrationDTO {

    private String phoneNumber;
    private String name;
    @JsonIgnore
    private MultipartFile photo;

}
