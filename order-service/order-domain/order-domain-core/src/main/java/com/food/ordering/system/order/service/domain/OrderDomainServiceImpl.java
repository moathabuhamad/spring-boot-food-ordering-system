package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCanceledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainExeption;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id:{} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id:{} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id:{} is approved", order.getId().getValue());
    }

    @Override
    public OrderCanceledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("payment for Order with id:{} is cancelling", order.getId().getValue());
        return new OrderCanceledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id:{} is canceled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant){
        if (!restaurant.isActive()){
            throw new OrderDomainExeption("Restaurant with id " + restaurant.getId().getValue() + " is not active");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        for (OrderItem orderItem : order.getItems()) {
            for (Product restaurantProduct : restaurant.getProducts()) {
                Product currentProduct = orderItem.getProduct();
                if (currentProduct.equals(restaurantProduct)) {
                    currentProduct.updateWothConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
                }
            }
        }
    }
}
