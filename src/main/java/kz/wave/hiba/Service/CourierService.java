package kz.wave.hiba.Service;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.DTO.CourierCreateDTO;
import kz.wave.hiba.DTO.CourierUpdateDTO;
import kz.wave.hiba.Entities.Courier;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Response.CourierOrderResponse;
import kz.wave.hiba.Response.CourierOrdersByButcheryResponse;
import kz.wave.hiba.Response.CourierResponse;
import kz.wave.hiba.Response.OrderResponse;

import java.util.List;

public interface CourierService {

    List<Courier> getAllCouriers();
    CourierResponse getCourier(Long id);
    Courier getCourierByFullNameOrPhoneOrEmail(String userName, String userPhone);
    Courier createCourier(CourierCreateDTO courierCreateDTO);
    Courier udpateCourier(CourierUpdateDTO courierUpdateDTO);
    Courier getCourierByUserId(Long userId);
    void deleteCourierById(Long id);
    List<CourierOrdersByButcheryResponse> getActiveOrdersByButchery(HttpServletRequest request);
    List<CourierOrdersByButcheryResponse> getWaitingOrdersByButchery();
    List<OrderResponse> getActiveOrdersByButcheryId(Long id, HttpServletRequest request);
    List<OrderResponse> getWaitingOrdersByButcheryId(Long id);
    List<CourierOrderResponse> getCouriers(String sort, String filter, String query);
    List<OrderResponse> getActiveOrders(HttpServletRequest request);
    List<OrderResponse> getWaitingOrders();
    Order verifyCode(Long id, String code, HttpServletRequest request);
}
