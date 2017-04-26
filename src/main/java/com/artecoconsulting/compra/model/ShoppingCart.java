package com.artecoconsulting.compra.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by arteco1 on 12/04/2017.
 */
public interface ShoppingCart {

    /**
     * Añande un item al carrito.
     *
     * @param item
     * @return
     */
    boolean addItem(Item item);

    /**
     * Devuelve los items que hay en el carrito.
     */
    List<Item> getItems();

    /**
     * Devuele la cantidad total de items que hay en el carrito
     */
    int getProductCount();

    /**
     * Crea un Order con el total de items del carrito y vacía el carrito.
     */
    Order checkout(Shop shop);

    /**
     * Si el cliente ha bloqueado un item y lo quiere anular, ese item vuelve a estar disponible en la tienda.
     *
     * @param item
     * @param shop
     */
    void removeItem(Item item, Shop shop);

    /**
     * Si el cliente quiere anular toda la compra, todos estos items vuelven a estar disponibles en la tienda.
     *
     * @param shop
     */
    void removeAll(Shop shop);

    /**
     * Al añadir items en el carrito, se va sumando al coste total del carrito.
     *
     * @return
     */
    BigDecimal getTotalCartPrice();


}
