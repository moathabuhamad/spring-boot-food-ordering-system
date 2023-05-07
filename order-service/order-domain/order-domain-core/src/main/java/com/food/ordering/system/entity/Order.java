package com.food.ordering.system.entity;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.domain.entity.AggrigateRoot;
import com.food.ordering.system.exception.OrderDomainExeption;
import com.food.ordering.system.valueobject.OrderItemId;
import com.food.ordering.system.valueobject.StreetAddress;
import com.food.ordering.system.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggrigateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId((UUID.randomUUID()));
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void  ValidateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO,Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainExeption("Total price: " + price.getAmount() + "does not equal Order Items Total:" + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()){
            throw new OrderDomainExeption("Order item price: "
                    + orderItem.getPrice().getAmount() + " is not valid for "
                    + orderItem.getProduct().getId().getValue()
            );
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()){
            throw new OrderDomainExeption("Total price must be greater than zero");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null){
            throw new OrderDomainExeption("Order state cannot be initialized");
        }
    }

    public void initializeOrderItems(){
        long itemID = 1;
        for (OrderItem orderItem: items){
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemID++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }
    }
}