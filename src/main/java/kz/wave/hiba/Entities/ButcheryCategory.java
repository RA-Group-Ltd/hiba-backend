package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "butchery_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @MapsId("butcheryId")
    @Column(name = "butchery_id")
    private Long butcheryId;

    @MapsId("categoryId")
    @Column(name = "category_id")
    private Long categoryId;

}
