package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Category} entities.
 */
@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Checks if a category with the given name exists.
     *
     * @param name the name of the category
     * @return true if a category with the given name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Finds a category by its name.
     *
     * @param name the name of the category
     * @return an {@link Optional} containing the category if found, or empty if not found
     */
    Optional<Category> findByName(String name);
}
