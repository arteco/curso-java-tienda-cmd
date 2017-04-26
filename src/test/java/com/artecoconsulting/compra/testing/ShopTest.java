package com.artecoconsulting.compra.testing;

import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.memory.InMemoryEnvironment;
import com.artecoconsulting.compra.memory.InMemoryShoppingCart;
import com.artecoconsulting.compra.memory.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by arteco1 on 12/04/2017.
 */
public class ShopTest {

    private Environment env = new InMemoryEnvironment();

    /**
     * Comprueba que tras añadir un elemento en la tienda, ese esté disponible para su compra
     */
    @Test
    public void saveItemTest() {
        Shop shop = env.getShop();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        shop.saveItem(raton);
        boolean found = false;
        for (Item item : shop.getItems()) {
            //comprueba si el item de la iteración es ratón
            // si lo encuentra found = true
            if (item.getId()!= null) {
                found = true;
                break;
            }

        }
        assertTrue(found);
    }

    /**
     * Comprueba si se ha eliminado un item de la tienda al no estar disponible en el stock.
     */

    @Test
    public void removeItemTest() {
        Shop shop = env.getShop();
        Item item1 = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        Item item2 = new Item("item1", 102L, new BigDecimal(11.0), 1);
        shop.saveItem(item1);
        shop.saveItem(item2);
        shop.removeItem(101L);
        shop.removeItem(102L);
        assertEquals(0, shop.getItems().size());
    }

    /**
     * Comprueba si al reservar un item pasa al carrito disminuyendo en la cantiad del stock
     */
    @Test
    public void reserveItemTest() throws NotAvailableItem {
        Shop shop = env.getShop();
        ShoppingCart cart = new InMemoryShoppingCart();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        //añadimos el item al stock
        shop.saveItem(raton);
        shop.reserveItem(101L, 1, cart);
        boolean found = false;
        for (Item item : cart.getItems()) {
            if (item.getId().equals(101L)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Comprueba la cantidad total que hay en stock de un item.
     */
    @Test
    public void getTotalQuantityTest() {
        Shop shop = env.getShop();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 50);
        shop.saveItem(raton);
        assertEquals(50, shop.getTotalQuantity(101L));
    }

    /**
     * Comprueba si se ha guardado la venta en la lista de ordenes finalizados.
     */
    @Test
    public void getOrdersTest() throws NotAvailableItem {
        // inicializaciones
        ShoppingCart cart = new InMemoryShoppingCart();
        ShoppingCart cart1 = new InMemoryShoppingCart();

        Shop shop = env.getShop();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 2);
        Item item1 = new Item("item1", 102L, new BigDecimal(11.0), 2);
        //añadimos el item al stock
        shop.saveItem(raton);
        shop.saveItem(item1);

        // compramos el item
        shop.reserveItem(101L, 1, cart);
        shop.reserveItem(102L, 2, cart1);

       // Order pedido = cart.checkout(shop);
        //Order pedido1 = cart1.checkout(shop);
        cart.checkout(shop);
        cart1.checkout(shop);

        assertEquals(2, shop.getOrders().size());

        boolean found = false;
        for (Order order : shop.getOrders()) {
            for (Item item : order.getOrders()) {
                if (item.getId().equals(101L)) {
//                    (item.getId().equals(pedido.getOrders().get(0).getId())
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found);

    }
}
