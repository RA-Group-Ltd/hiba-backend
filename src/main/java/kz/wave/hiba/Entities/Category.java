package kz.wave.hiba.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/**
 * This class represents a category.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    /**
     * The name of the category.
     */
    @Column(name = "name")
    private String name;

    /**
     * The identifier of the parent category, if any.
     */
    @Column(name = "parent_category_id")
    private Long parentCategoryId;

}
