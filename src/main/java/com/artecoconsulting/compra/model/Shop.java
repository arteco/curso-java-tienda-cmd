package com.artecoconsulting.compra.model;

import com.artecoconsulting.compra.common.NotAvailableItem;

import java.util.List;

/**
 * Created by arteco1 on 12/04/2017.
 */
public interface Shop {

    /**
     * Consulta un item del stock.
     * Esta operación permite consultar un producto según su id.
     *
     * @param id del item a localizar en BD.
     * @return item con id = al parámetro o null si no encuentra el item
     */
    Item getItem(Long id);

    /**
     * Guarda el item dentro del stock de la tienda
     * Si item.id es null, se crea un producto, si
     * por el contrario item.id!=null entonces se actualiza el producto
     */
    boolean saveItem(Item item);

    /**
     * Elimina un item del stock, independientemente de cuantos elementos haya
     */
    boolean removeItem(Long id);

    /**
     * Devuelve todos los elementos disponibles en la tienda (stock)
     */
    List<Item> getItems();

    /**
     * Bloquea un item del stock decrementando su cantidad pasándolo al carrito.
     * Si cantidad == 0 debe eliminar el item de la tienda.
     *
     * @param idItem
     * @param itemQuantity
     * @param cart
     */
    void reserveItem(Long idItem, int itemQuantity, ShoppingCart cart) throws NotAvailableItem;

    /**
     * Devuelve el número de items del mismo tipo disponibles en el stock
     *
     * @param id de item
     */
    int getTotalQuantity(Long id);

    /**
     * Añande todas las ventas realizadas a la lista de ventas.
     *
     * @param order
     */
    void addOrder(Order order);

    /**
     * Devuelve todas las ventas realizadas
     */
    List<Order> getOrders();

}
