package kz.wave.hiba.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryOrderStats {
    private int activeOrders;
    private int newOrders;
    private int deliveredOrders;
}
