package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.WorkingHours;
import kz.wave.hiba.Enum.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findAllByButcheryId(Long id);

    WorkingHours findWorkingHoursByButcheryIdAndDayOfWeek(Long butchery_id, DayOfWeek dayOfWeek);
}
