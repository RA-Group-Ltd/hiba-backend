package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.wave.hiba.Enum.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class represents the working hours of a butchery.
 */
@Entity
@Table(name = "working_hours")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHours {

    /**
     * The unique identifier of the working hours entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The butchery associated with these working hours.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "butcher_id")
    private Butchery butchery;

    /**
     * The day of the week for these working hours.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    /**
     * Indicates whether the butchery is closed on this day.
     */
    @Column(name = "is_closed")
    private boolean isClosed;

    /**
     * The opening time of the butchery on this day.
     */
    @Column(name = "open_time")
    private String openTime;

    /**
     * The closing time of the butchery on this day.
     */
    @Column(name = "close_time")
    private String closeTime;
}
