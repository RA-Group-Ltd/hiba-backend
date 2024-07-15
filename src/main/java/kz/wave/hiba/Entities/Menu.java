package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class represents the menu.
 */
@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    /**
     * The unique identifier of the menu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The name of the menu.
     */
    @Column(name = "name")
    private String name;

    /**
     * The weight of the menu.
     */
    @Column(name = "weight")
    private int weight;

    /**
     * Indicates whether the menu consists of a whole animal.
     */
    @Column(nullable = false, name = "is_whole_animal")
    private Boolean isWholeAnimal;

    /**
     * The price of the menu.
     */
    @Column(name = "price", columnDefinition = "int default 0")
    private int price;

    /**
     * The identifier of the butchery category associated with the menu.
     */
    @Column(name = "butcheryCategoryId")
    private Long butcheryCategoryId;

    /**
     * The identifier of the category associated with the menu.
     */
    @Column(name = "categoryId")
    private Long categoryId;

    /**
     * The image of the menu.
     */
    @Column(name = "image")
    private byte[] image;

    /**
     * The description of the menu.
     */
    @Column(name = "description")
    private String description;

}
