package com.artecoconsulting.compra.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by arteco1 on 12/04/2017.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    private String nombre;
    private Integer cantidad;
    private BigDecimal precio;

    public boolean equals(Object arg){
        boolean result;
        if((arg == null) || (getClass() != arg.getClass())){
            result = false;
        }
        else{
            Item item = (Item) arg;
            result = id.equals(item.getId());
        }
        return result;
    }
}
