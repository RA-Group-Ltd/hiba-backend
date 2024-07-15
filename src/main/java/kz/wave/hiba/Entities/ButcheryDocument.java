package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a document associated with a butchery.
 */
@Entity
@Table(name = "butchery_document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryDocument {

    /**
     * The unique identifier of the butchery document.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The image of the certificate associated with the butchery.
     */
    @Column(name = "certificate_image")
    private byte[] certificateImage;

    /**
     * The butchery associated with this document.
     */
    @ManyToOne
    @JoinColumn(name = "butchery_id")
    private Butchery butchery;

}
