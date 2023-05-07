package com.food.ordering.system.exception;

import com.food.ordering.system.domain.exeption.DomainExeption;

public class OrderDomainExeption extends DomainExeption {

    public OrderDomainExeption(String message) {
        super(message);
    }

    public OrderDomainExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
