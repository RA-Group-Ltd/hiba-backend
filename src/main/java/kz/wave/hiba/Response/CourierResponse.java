package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Courier;
import kz.wave.hiba.Entities.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourierResponse {

    private Courier courier;
    private Long deliveredOrders;
    private Long activeOrders;

}
