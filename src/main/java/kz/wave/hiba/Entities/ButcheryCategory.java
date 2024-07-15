package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents the category of a butchery.
 */
@Entity
@Table(name = "butchery_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryCategory {

    /**
     * The unique identifier of the butchery category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The identifier of the butchery associated with this category.
     */
    @MapsId("butcheryId")
    @Column(name = "butchery_id")
    private Long butcheryId;

    /**
     * The category associated with this butchery.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;

    /*
     * // Uncomment this method if you need to retrieve the category ID
     * public Long getCategoryId() {
     *     return this.categoryId.getId();
     * }
     */
}
