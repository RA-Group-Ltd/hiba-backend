package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "couriers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rating")
    private double rating = 0.0;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "created_at")
    private Instant createdAt;
}
