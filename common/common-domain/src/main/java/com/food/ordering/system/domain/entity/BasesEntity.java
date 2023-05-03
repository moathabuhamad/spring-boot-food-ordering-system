package com.food.ordering.system.domain.entity;

import java.util.Objects;

public class BasesEntity <ID>{
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return  false ;
        }

        BasesEntity<?> that = (BasesEntity<?>) obj;
        return id.equals(that.id);
    }
}
