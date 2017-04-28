package com.artecoconsulting.compra.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by arteco1 on 20/04/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private List<Item> orders;
    private Long idOrder;

    public Order(List<Item> items) {
        this.orders = items;
    }



    public BigDecimal getTotalPrice() {
        throw new NotImplementedException();
    }
}
