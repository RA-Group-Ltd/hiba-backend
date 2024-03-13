package kz.wave.hiba.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "butchers")
public class Butcher extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "butcheryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Butchery butchery;

}
