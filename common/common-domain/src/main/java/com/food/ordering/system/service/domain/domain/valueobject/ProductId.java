package com.food.ordering.system.service.domain.domain.valueobject;

import java.util.UUID;

public class ProductId extends BaseId<UUID>{
    public ProductId(UUID value){
        super(value);
    }
}
