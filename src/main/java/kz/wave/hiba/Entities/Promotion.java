package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.Audience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * The class represents a promotion.
 */
@Entity
@Table(name = "promotion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {

    /**
     * The unique identifier of the promotion.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The title of the promotion.
     */
    @Column(name = "title")
    private String title;

    /**
     * The description of the promotion.
     */
    @Column(name = "description")
    private String description;

    /**
     * The image associated with the promotion.
     */
    @Column(name = "image")
    private byte[] image;

    /**
     * The target audience of the promotion.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "audience")
    private Audience audience = Audience.CLIENTS;

    /**
     * The date and time when the promotion was created.
     */
    @Column(name = "created_at")
    private Instant createdAt;

}
