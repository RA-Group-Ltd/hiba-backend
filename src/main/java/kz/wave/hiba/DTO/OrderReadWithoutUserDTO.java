package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.wave.hiba.Entities.Address;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadWithoutUserDTO {

    private Long id;
    private OrderStatus orderStatus;
    private Address address;
    private Butchery butchery;
    private boolean isCharity;
    private Map<Long, Integer> menuItems;
    private Instant deliveryDate;
    private double totalPrice;
    private double deliveryPrice;
    private double donation;
    private int packages;


}
