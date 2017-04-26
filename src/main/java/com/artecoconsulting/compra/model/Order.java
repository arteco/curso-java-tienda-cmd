package com.artecoconsulting.compra.model;

import lombok.Data;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by arteco1 on 20/04/2017.
 */
@Data
public class Order {
    List<Item> orders;

    public Order(List<Item> items) {
        this.orders = items;
    }

    public BigDecimal getTotalPrice() {
        throw new NotImplementedException();
    }
}
