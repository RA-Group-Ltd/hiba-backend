package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "butcheries")
@Getter
@Setter
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

    @Column(name = "phone")
    private String phone;

    @Column(name = "image")
    private byte[] image;

    @OneToMany(mappedBy = "butchery", cascade = CascadeType.ALL)
    private List<ButcheryDocument> documents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
}
