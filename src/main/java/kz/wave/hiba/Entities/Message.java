package kz.wave.hiba.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "chat")
    private Chat chat;

    @OneToOne
    @JoinColumn(name = "sender")
    private User sender;

    @Column(name = "content")
    private String content;

    @Column(name = "images")
    private byte[] images;

    @Column(name = "created_at")
    private Instant createAt;

}
