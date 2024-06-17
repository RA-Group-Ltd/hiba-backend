package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryOrderStats {
    private int activeOrders;
    private int newOrders;
    private int deliveredOrders;
}
