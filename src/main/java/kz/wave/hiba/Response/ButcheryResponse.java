package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Category;
import kz.wave.hiba.Entities.City;
import kz.wave.hiba.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ButcheryResponse {
    private Butchery butchery;
    private List<ButcheryCategoryResponse> categories;
    private List<User> employees;
    private int activeOrders;
    private int deliveredOrders;
}
