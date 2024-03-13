package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "butcheries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Butchery extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;

}
