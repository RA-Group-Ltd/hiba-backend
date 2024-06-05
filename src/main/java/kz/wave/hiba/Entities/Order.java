package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "butchery_id")
    private Butchery butchery;

    @Column(name = "is_charity")
    private boolean isCharity = false;

    @ElementCollection
    @CollectionTable(name = "order_menu_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "count", nullable = false)
    private Map<Menu, Integer> menuItems = new HashMap<>();

    @Column(name = "delivery_date")
    private Instant deliveryDate = null;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "total_price")
    private double totalPrice = 0.0;

    @Column(name = "delivery_price")
    private double deliveryPrice = 0.0;

    @Column(name = "donation")
    private double donation = 0.0;

    @Column(name = "sender")
    private String sender = null;

    @Column(name = "packages")
    private int packages = 1;

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = true)
    private Courier courier;
}
