package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Butchery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourierOrdersByButcheryResponse {

    private Butchery butchery;
    private int activeOrders;

}
