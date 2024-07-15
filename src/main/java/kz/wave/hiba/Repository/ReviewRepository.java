package kz.wave.hiba.Repository;

import jakarta.transaction.Transactional;
import kz.wave.hiba.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Review} entities.
 */
@Repository
@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Finds reviews by their ID.
     *
     * @param id the ID of the review
     * @return a list of reviews with the specified ID
     */
    List<Review> findReviewsById(Long id);

    /**
     * Finds reviews by butchery ID, ordered by creation date in descending order.
     *
     * @param id the ID of the butchery
     * @return a list of reviews associated with the butchery, ordered by creation date in descending order
     */
    List<Review> findReviewsByButcheryIdIdOrderByCreatedAtDesc(Long id);
}
