package kz.wave.hiba.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Enum.OrderStatus;
import kz.wave.hiba.Entities.Address;
import kz.wave.hiba.Entities.Butcher;
import kz.wave.hiba.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {

    @JsonIgnore
    private Long id;
    private OrderStatus orderStatus;
    private Address address;
    @JsonIgnore
    private User user;
    private Butchery butchery;
    private boolean isCharity;
    private Map<Long, Integer> menuItemsId;
    private double donation;
    private double deliveryPrice;
    private double totalPrice;
    private Long deliveryDate;
    private String sender;
    private int packages;

}
