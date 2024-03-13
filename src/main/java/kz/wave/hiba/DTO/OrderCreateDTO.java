package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Entities.Address;
import kz.wave.hiba.Entities.Butcher;
import kz.wave.hiba.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {

    @JsonIgnore
    private Long id;
    private OrderStatus orderStatus;
    private Address address;
    @JsonIgnore
    private User user;
    private Butcher butcher;

}
