package kz.wave.hiba.Enum;

/**
 * The enum represents the status of an order.
 */
public enum OrderStatus {
    /**
     * The order is awaiting confirmation.
     */
    AWAITING_CONFIRMATION,

    /**
     * The order is in process.
     */
    IN_PROCESS,

    /**
     * The order is being prepared for delivery.
     */
    PREPARING_FOR_DELIVERY,

    /**
     * The order has been received.
     */
    RECEIVED,

    /**
     * The order is on the way.
     */
    ON_THE_WAY,

    /**
     * The order has been delivered.
     */
    DELIVERED,

    /**
     * The delivery is scheduled for tomorrow.
     */
    DELIVERY_TOMORROW,

    /**
     * The order has been canceled.
     */
    CANCEL
}
