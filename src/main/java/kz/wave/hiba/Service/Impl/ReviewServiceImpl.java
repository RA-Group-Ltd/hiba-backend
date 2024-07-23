package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.DTO.ReviewCreateDTO;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Review;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Repository.ReviewRepository;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ReviewService} interface.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ButcheryRepository butcheryRepository;

    /**
     * Retrieves the reviews made by a user with the given ID.
     *
     * @param id the ID of the user
     * @return a list of reviews made by the user
     */
    @Override
    public List<Review> getMyReviews(Long id) {
        return reviewRepository.findReviewsById(id);
    }

    /**
     * Retrieves the reviews for a butchery with the given ID, ordered by creation date in descending order.
     *
     * @param id the ID of the butchery
     * @return a list of reviews for the butchery
     */
    @Override
    public List<Review> getTeacherReviews(Long id) {
        return reviewRepository.findReviewsByButcheryIdIdOrderByCreatedAtDesc(id);
    }

    /**
     * Creates a new review for a butchery.
     *
     * @param reviewCreateDTO the data transfer object containing review creation details
     * @param username the username of the reviewer
     * @param butcheryId the ID of the butchery
     * @return the created review entity, or null if the butchery or reviewer does not exist
     */
    @Override
    public Review createReview(ReviewCreateDTO reviewCreateDTO, String username, Long butcheryId) {
        Review newReview = new Review();

        newReview.setText(reviewCreateDTO.getText());
        newReview.setRate(reviewCreateDTO.getRate());

        Optional<Butchery> butcheryOptional = butcheryRepository.findById(butcheryId);
        User reviewer = userRepository.findByPhone(username);

        if (butcheryOptional.isEmpty() || reviewer == null) {
            return null;
        }

        Butchery butchery = butcheryOptional.get();

        newReview.setButcheryId(butchery);
        newReview.setReviewerId(reviewer);
        newReview.setCreatedAt(Instant.now());

        return reviewRepository.save(newReview);
    }
}
