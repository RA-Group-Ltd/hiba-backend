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

    @Column(nullable = false)
    private Boolean isWholeAnimal;

    @ManyToOne
    @JoinColumn(name = "butcheryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Butchery butchery;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;


}
