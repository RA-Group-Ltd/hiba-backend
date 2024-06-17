package kz.wave.hiba.DTO;

import kz.wave.hiba.Enum.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHourDTO {
    private Long id;
    private DayOfWeek dayOfWeek;
    private boolean isClosed;
    private String openTime;
    private String closeTime;
}
