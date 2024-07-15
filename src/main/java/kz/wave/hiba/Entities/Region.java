package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * The class represents a region.
 */
@Entity
@Table(name = "regions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Region extends BaseEntity {

    /**
     * The name of the region.
     */
    @Column(name = "name")
    private String name;

    /**
     * The country to which the region belongs.
     */
    @ManyToOne
    @JoinColumn(name = "countryId", nullable = false)
    private Country country;

}
