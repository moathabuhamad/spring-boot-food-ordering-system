package com.food.ordering.system.domain.exeption;

public class DomainExeption extends RuntimeException {

    public DomainExeption(String message){
        super(message);
    }

    public DomainExeption(String message,Throwable cause){
        super(message, cause);
    }
}
