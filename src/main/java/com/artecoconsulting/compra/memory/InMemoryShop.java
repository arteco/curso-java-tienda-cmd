package com.artecoconsulting.compra.memory;

import com.artecoconsulting.compra.common.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arteco1 on 20/04/2017.
 */
public class InMemoryShop implements Shop {

    private List<Item> items = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    @Override
    public Item getItem(Long id) {
        for (Item item : items) {
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean saveItem(Item item) {
        boolean guradado = false;
        if (item.getId() == null) {
            // Debemos crear un id para poder referencialo de manera unequívoca después.
            item.setId((long) items.size());
            items.add(item);
            guradado = true;

        } else {
            // Hay que encontrarlo, y sobre el que ya existe actualizar los atributos
            Item dbItem = getItem(item.getId());
            if (dbItem != null) {
                dbItem.setNombre(item.getNombre());
                dbItem.setPrecio(item.getPrecio());
                dbItem.setCantidad(item.getCantidad());
                guradado = true;
                // dbItem.setId(); el ID no se debe modificar, una vez asignado debe
                // permanecer siempre el mismo para poder localizar el item en la BD.

                // si  fuera una app de producción habría que envíarel item a
                // la base de datos para hacer persistentes lo cambios.

            } else {
                // si no se ha encontrado el item en bd  se puede añadir, ya que
                // no se ha encontrado, o lanzar un error (throw new Exception())
                items.add(item);
            }
        }
        return guradado;
    }

    @Override
    public boolean removeItem(Long id) {
        Item item = getItem(id);
        if (item != null) {
            items.remove(item);
            return true;
        }
        return false;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }


    @Override
    public void reserveItem(Long idItem, int itemQuantity, ShoppingCart cart) throws NotAvailableItem {
        // Añadir item al carrito
        // Si hay menos cantidad del item de la se solcita, mostrar lo que hay
        // Actualizar la cantidad del item según lo que se haya añadidio al carrito.
        // Si al reservar un item la cantidad de ese item se queda en  0, quitarla del shop.
        Item itemShop = getItem(idItem);
        if (itemShop != null && itemShop.getCantidad() >= itemQuantity) {
            // podemos pasar el item al carrito
            Item itemCart = new Item(
                    itemShop.getId(),
                    itemShop.getNombre(),
                    itemQuantity,
                    itemShop.getPrecio());
            cart.addItem(itemCart);
            itemShop.setCantidad(itemShop.getCantidad() - itemQuantity);
            // si nos quedamos sin items disponibles lo eliminamos de la tienda
            if (itemShop.getCantidad() <= 0) {
                removeItem(itemShop.getId());
            }
        } else {
            throw new NotAvailableItem();
        }
    }

    @Override
    public int getTotalQuantity(Long id) {
        int cantidad = 0;
        for (Item item : items) {
            cantidad += item.getCantidad();
        }
        return cantidad;

    }

    @Override
    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
        }
    }


    @Override
    public List<Order> getOrders() {
        return orders;
    }


}
