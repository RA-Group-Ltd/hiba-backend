package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a butcher.
 */
@Entity
@Table(name = "butchers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Butcher {

    /**
     * The unique identifier of the butcher.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The user associated with this butcher.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The butchery associated with this butcher.
     */
    @ManyToOne
    @JoinColumn(name = "butchery_id")
    private Butchery butchery;

}
