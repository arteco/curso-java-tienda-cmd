package com.artecoconsulting.compra.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Simarjeet Singh on 20/04/2017.
 */
@Data
@NoArgsConstructor
public class Order {
    private List<Item> orders;
    private Long idOrder;

    public Order(List<Item> items) {
        this.orders = items;
    }

    /**
     * Calcula el importe total de las ventas.
     * @return
     */
    public BigDecimal getTotalPrice() {
        throw new IllegalArgumentException();
    }

}
