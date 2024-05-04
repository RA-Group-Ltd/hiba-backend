package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "butchery_document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_image")
    private byte[] certificateImage;

    @ManyToOne
    @JoinColumn(name = "butchery_id")
    private Butchery butchery;

}
