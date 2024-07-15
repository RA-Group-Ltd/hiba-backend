package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Promotion} entities.
 */
@Repository
@Transactional
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    /**
     * Finds a promotion by its title.
     *
     * @param title the title of the promotion
     * @return the promotion with the specified title
     */
    Promotion findPromotionByTitle(String title);
}
