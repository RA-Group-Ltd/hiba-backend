package kz.wave.hiba.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


/**
 * The class represents a country
 */
@Entity
@Table(name = "countries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Country extends BaseEntity {

    /**
     * name of the country
     */
    @Column(name = "name")
    private String name;

}
