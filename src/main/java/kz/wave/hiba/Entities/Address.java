package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an address.
 */
@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    /**
     * The unique identifier of the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The street address.
     */
    @Column(name = "address")
    private String address;

    /**
     * The building number.
     */
    @Column(name = "building")
    private String building;

    /**
     * The entrance number.
     */
    @Column(name = "entrance")
    private String entrance;

    /**
     * The apartment number.
     */
    @Column(name = "apartment")
    private String apartment;

    /**
     * The floor number.
     */
    @Column(name = "floor")
    private String floor;

    /**
     * The name of the resident or recipient.
     */
    @Column(name = "name")
    private String name;

    /**
     * The user associated with this address.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    /**
     * The city associated with this address.
     */
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

}
