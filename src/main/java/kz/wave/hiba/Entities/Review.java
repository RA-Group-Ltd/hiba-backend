package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {

    @Column(name = "text")
    private String text;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "createdAt")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buthchery_id")
    private Butchery butcheryId;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewerId;



}
