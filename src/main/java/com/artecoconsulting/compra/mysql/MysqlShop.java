package com.artecoconsulting.compra.mysql;

import com.artecoconsulting.compra.common.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;

import java.util.List;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class MysqlShop implements Shop {

    private final Database database;

    public MysqlShop(Database database) {
        this.database = database;
    }

    @Override
    public Item getItem(Long id) {
        return database.getItem(id);
    }

    @Override
    public boolean saveItem(Item item) {
        boolean guradado = false;
        if (item.getId() == null) {
            // Debemos crear un id para poder referencialo de manera unequívoca después.
            long numItems = database.countItems();
            item.setId(numItems);
            database.saveItem(item);
            guradado = true;

        } else {
            // Hay que encontrarlo, y sobre el que ya existe actualizar los atributos
            Item dbItem = getItem(item.getId());
            if (dbItem != null) {
                dbItem.setNombre(item.getNombre());
                dbItem.setPrecio(item.getPrecio());
                dbItem.setCantidad(item.getCantidad());
                database.saveItem(dbItem);
                guradado = true;
                // dbItem.setId(); el ID no se debe modificar, una vez asignado debe
                // permanecer siempre el mismo para poder localizar el item en la BD.

                // si  fuera una app de producción habría que envíarel item a
                // la base de datos para hacer persistentes lo cambios.

            } else {
                // si no se ha encontrado el item en bd  se puede añadir, ya que
                // no se ha encontrado, o lanzar un error (throw new Exception())
                database.saveItem(item);
            }
        }
        return guradado;
    }

    @Override
    public boolean removeItem(Long id) {
        Item item = getItem(id);
        if (item != null) {
            database.removeItem(item);
            return true;
        }
        return false;
    }

    @Override
    public List<Item> getItems() {
        return database.getItems();
    }


    @Override
    public void reserveItem(Long id, int itemQuantity, ShoppingCart cart) throws NotAvailableItem {
        // Añadir item al carrito
        // Si hay menos cantidad del item de la se solcita, mostrar lo que hay
        // Actualizar la cantidad del item según lo que se haya añadidio al carrito.
        // Si al reservar un item la cantidad de ese item se queda en  0, quitarla del shop.
        Item itemShop = database.getItem(id);
        if (itemShop != null && itemShop.getCantidad() >= itemQuantity) {
            // podemos pasar el item al carrito
            Long cartId = cart.getId();
            database.saveCartItem(cartId, itemShop, itemQuantity);
            itemShop.setCantidad(itemShop.getCantidad() - itemQuantity);
            // si nos quedamos sin items disponibles lo eliminamos de la tienda
            if (itemShop.getCantidad() <= 0) {
                removeItem(itemShop.getId());
            } else {
                database.saveItem(itemShop);
            }
        } else {
            throw new NotAvailableItem();
        }
    }

    @Override
    public int getTotalQuantity(Long id) {
        int cantidad = 0;
        for (Item item : database.getItems()) {
            if (item.getId().equals(id)) {
                cantidad += item.getCantidad();
            }
        }
        return cantidad;
    }

    @Override
    public void addOrder(Order order) {
        if (order != null) {
            database.saveOrder(order);
        }
    }

    @Override
    public List<Order> getOrders() {
        return database.getOrders();
    }

}


