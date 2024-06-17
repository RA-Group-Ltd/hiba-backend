package kz.wave.hiba.DTO;

import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.WorkingHours;
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
    private String phone;
    private String address;
    private Long city;
    private MultipartFile image;
    private List<WorkingHourDTO> workingHours;

}
