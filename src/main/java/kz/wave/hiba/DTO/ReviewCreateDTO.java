package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDTO {

    private String text;
    private Integer rate;
    @JsonIgnore
    private Butchery butcheryId;
    @JsonIgnore
    private User reviewerId;
    private Instant createdAt;

}
