package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private int weight;

    @Column(nullable = false, name = "is_whole_animal")
    private Boolean isWholeAnimal;

    @Column(name = "butcheryCategoryId")
    private Long butcheryCategoryId;

    @Column(name = "categoryId")
    private Long categoryId;

}
