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

    private String nombre;
    private Long id;
    private BigDecimal precio;
    private Integer cantidad;


}
