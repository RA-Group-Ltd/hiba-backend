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
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private int weight;

    @Column(nullable = false, name = "is_whole_animal")
    private Boolean isWholeAnimal;

    @Column(name = "price", columnDefinition = "int default 0")
    private int price;

    @Column(name = "butcheryCategoryId")
    private Long butcheryCategoryId;

    @Column(name = "categoryId")
    private Long categoryId;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "description")
    private String description;

}
