package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * The class represents a review.
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {

    /**
     * The text of the review.
     */
    @Column(name = "text")
    private String text;

    /**
     * The rating given in the review.
     */
    @Column(name = "rate")
    private Integer rate;

    /**
     * The date and time when the review was created.
     */
    @Column(name = "createdAt")
    private Instant createdAt;

    /**
     * The butchery associated with the review.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buthchery_id")
    private Butchery butcheryId;

    /**
     * The user who wrote the review.
     */
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewerId;

}
