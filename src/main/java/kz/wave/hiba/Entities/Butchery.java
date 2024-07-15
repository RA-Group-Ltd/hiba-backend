package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a butchery.
 */
@Entity
@Table(name = "butcheries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Butchery extends BaseEntity{

    /**
     * The name of the butchery.
     */
    @Column(name = "name")
    private String name;

    /**
     * The latitude of the butchery's location.
     */
    @Column(name = "latitude")
    private float latitude = 0.0f;

    /**
     * The longitude of the butchery's location.
     */
    @Column(name = "longitude")
    private float longitude = 0.0f;

    /**
     * The registration number of the butchery.
     */
    @Column(name = "reg_number")
    private String regNumber;

    /**
     * The email address of the butchery.
     */
    @Column(name = "email")
    private String email;

    /**
     * The type of meat provided by the butchery.
     */
    @Column(name = "meat_type")
    private String meatType;

    /**
     * The physical address of the butchery.
     */
    @Column(name = "address")
    private String address;

    /**
     * The date and time when the butchery was created.
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * The phone number of the butchery.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * The image of the butchery.
     */
    @Column(name = "image")
    private byte[] image;

    /**
     * The list of documents associated with the butchery.
     */
    @OneToMany(mappedBy = "butchery", cascade = CascadeType.ALL)
    private List<ButcheryDocument> documents = new ArrayList<>();

    /**
     * The city where the butchery is located.
     */
    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;

    /**
     * The owner of the butchery.
     */
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
}
