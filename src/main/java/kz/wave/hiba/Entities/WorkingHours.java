package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.wave.hiba.Enum.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.Instant;

@Entity
@Table(name = "working_hours")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "butcher_id")
    private Butchery butchery;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;
}
