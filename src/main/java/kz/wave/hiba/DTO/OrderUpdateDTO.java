package kz.wave.hiba.DTO;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {

    private Long id;
    private OrderStatus orderStatus;
    private Address address;
    private User user;

}
