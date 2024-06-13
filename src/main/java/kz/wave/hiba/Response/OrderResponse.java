package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Entities.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Order order;
    private List<OrderMenuResponse> menuList;

}
