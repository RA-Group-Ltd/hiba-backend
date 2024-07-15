package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * the class represents a courier
 */
@Entity
@Table(name = "couriers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Courier {

    /**
     * The unique identifier of the courier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Identifier which user is courier
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Courier's rating
     */
    @Column(name = "rating")
    private double rating = 0.0;

    /**
     * Courier's from of the city
     */
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    /**
     * Account created date
     */
    @Column(name = "created_at")
    private Instant createdAt;
}
