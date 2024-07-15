package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Base entity class that provides a common ID field for all entities.
 */
@MappedSuperclass
@Data
public class BaseEntity {

    /**
     * The unique identifier of the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

}
