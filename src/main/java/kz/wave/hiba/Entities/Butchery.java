package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "butcheries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Butchery extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private float latitude = 0.0f;

    @Column(name = "longitude")
    private float longitude = 0.0f;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "meat_type")
    private String meatType;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
}
