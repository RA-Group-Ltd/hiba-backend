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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ButcheryRepository butcheryRepository;

    @Override
    public List<Review> getMyReviews(Long id) {
        return reviewRepository.findReviewsById(id);
    }

    @Override
    public List<Review> getTeacherReviews(Long id) {
        return reviewRepository.findReviewsByButcheryIdIdOrderByCreatedAtDesc(id);
    }

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
