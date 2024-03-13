package kz.wave.hiba.Service;

import kz.wave.hiba.DTO.ReviewCreateDTO;
import kz.wave.hiba.Entities.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getMyReviews(Long id);
    List<Review> getTeacherReviews(Long id);
    Review createReview(ReviewCreateDTO reviewCreateDTO, String username, Long butcheryId);

}
