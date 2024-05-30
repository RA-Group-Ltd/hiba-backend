package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "butchers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Butcher extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "butcheryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Butchery butchery;

}
