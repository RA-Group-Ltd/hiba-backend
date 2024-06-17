package kz.wave.hiba.Service;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.DTO.OrderCreateDTO;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Response.OrderResponse;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
    OrderResponse getOneOrder(Long id);
    Order createOrder(OrderCreateDTO orderCreateDTO, HttpServletRequest request);
    Order updateOrder(OrderUpdateDTO orderUpdateDTO, HttpServletRequest request);
    Order updateOrderStatus(Long id, HttpServletRequest request, OrderStatus newOrderStatus);
    List<OrderResponse> getMyOrders(Long id);
    List<Order> getMyActiveOrders(Long id);
    long quantityOfOrders();
    public List<Order> getOrdersByCourier(Long courierId, List<String> filter, Long startDate, Long endDate);
    long getDeliveredOrdersByCourierId(Long courierId);
    List<Order> getOrders(String query, String period, List<String> statuses);

    Order cancelOrder(Long id);
}
