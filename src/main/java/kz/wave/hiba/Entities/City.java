package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents a city.
 */
@Entity
@Table(name = "cities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class City extends BaseEntity{

    /**
     * The name of the city.
     */
    @Column(name = "name")
    private String name;

    /**
     * The region to which the city belongs.
     */
    @ManyToOne
    @JoinColumn(name = "regionId")
    private Region region;

    /**
     * The country to which the city belongs.
     */
    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

}
