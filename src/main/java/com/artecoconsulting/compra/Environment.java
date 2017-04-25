package com.artecoconsulting.compra;

import com.artecoconsulting.compra.model.*;

import java.util.List;

/**
 * Created by arteco1 on 20/04/2017.
 */
public interface Environment {

    /**
     * Devuelve la instancia de la tienda sobre la que se va a trabajar
     * @return
     */
    Shop getShop();

}
