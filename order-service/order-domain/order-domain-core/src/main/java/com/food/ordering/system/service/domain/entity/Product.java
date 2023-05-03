package com.food.ordering.system.service.domain.entity;

import com.food.ordering.system.service.domain.domain.valueobject.Money;
import com.food.ordering.system.service.domain.domain.valueobject.ProductId;

public class Product extends BasesEntity<ProductId>{
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
