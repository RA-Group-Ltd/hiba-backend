package kz.wave.hiba.Service;

import jakarta.servlet.http.HttpServletRequest;
import kz.wave.hiba.DTO.OrderCreateDTO;
import kz.wave.hiba.DTO.OrderReadWithoutUserDTO;
import kz.wave.hiba.DTO.OrderUpdateDTO;
import kz.wave.hiba.Entities.Order;
import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Enum.OrderStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
    Order getOneOrder(Long id);
    Order createOrder(OrderCreateDTO orderCreateDTO, HttpServletRequest request);
    Order updateOrder(OrderUpdateDTO orderUpdateDTO, HttpServletRequest request);
    Order updateOrderStatus(Long id, HttpServletRequest request, OrderStatus newOrderStatus);
    List<Order> getMyOrders(Long id);
    List<Order> getMyActiveOrders(Long id);

}
