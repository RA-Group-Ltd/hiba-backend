package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.City;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourierOrderResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private City city;
    private Long quantityOrders;

    CourierOrderResponse(Long id, String name, String phone, String email, City city, Long quantityOrders ){
        this.id = id;
        this.city = city;
        this.quantityOrders = quantityOrders;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
