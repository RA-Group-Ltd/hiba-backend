package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.WorkingHours;
import kz.wave.hiba.Enum.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link WorkingHours} entities.
 */
@Repository
@Transactional
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

    /**
     * Finds all working hours entries by butchery ID.
     *
     * @param id the butchery ID
     * @return a list of working hours associated with the butchery ID
     */
    List<WorkingHours> findAllByButcheryId(Long id);

    /**
     * Finds a specific working hours entry by butchery ID and day of the week.
     *
     * @param butchery_id the butchery ID
     * @param dayOfWeek the day of the week
     * @return the working hours entry matching the butchery ID and day of the week
     */
    WorkingHours findWorkingHoursByButcheryIdAndDayOfWeek(Long butchery_id, DayOfWeek dayOfWeek);
}
