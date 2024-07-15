package kz.wave.hiba.Entities;

import jakarta.persistence.*;
import kz.wave.hiba.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * The class represents an order.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /**
     * The unique identifier of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The status of the order.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    /**
     * The address associated with the order.
     */
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    /**
     * The user who placed the order.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The butchery from which the order is made.
     */
    @ManyToOne
    @JoinColumn(name = "butchery_id")
    private Butchery butchery;

    /**
     * Indicates whether the order is for charity.
     */
    @Column(name = "is_charity")
    private boolean isCharity = false;

    /**
     * The menu items and their counts in the order.
     */
    @ElementCollection
    @CollectionTable(name = "order_menu_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "count", nullable = false)
    private Map<Long, Integer> menuItems = new HashMap<>();

    /**
     * The date and time when the order should be delivered.
     */
    @Column(name = "delivery_date")
    private Instant deliveryDate = null;

    /**
     * The date and time when the order was created.
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * The total price of the order.
     */
    @Column(name = "total_price")
    private double totalPrice = 0.0;

    /**
     * The delivery price of the order.
     */
    @Column(name = "delivery_price")
    private double deliveryPrice = 0.0;

    /**
     * The donation amount associated with the order.
     */
    @Column(name = "donation")
    private double donation = 0.0;

    /**
     * The sender of the order.
     */
    @Column(name = "sender")
    private String sender = null;

    /**
     * The number of packages in the order.
     */
    @Column(name = "packages")
    private int packages = 1;

    /**
     * The courier responsible for delivering the order.
     */
    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = true)
    private Courier courier;
}
