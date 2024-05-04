package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.Audience;
import kz.wave.hiba.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "promotion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private byte[] image;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience")
    private Audience audience = Audience.CLIENTS;

    @Column(name = "created_at")
    private Instant createdAt;

}
