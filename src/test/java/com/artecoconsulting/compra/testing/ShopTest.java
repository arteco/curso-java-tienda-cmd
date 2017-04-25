package com.artecoconsulting.compra.testing;

import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.memory.InMemoryEnvironment;
import com.artecoconsulting.compra.memory.InMemoryShoppingCart;
import com.artecoconsulting.compra.memory.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;
import com.sun.deploy.util.OrderedHashSet;
import com.sun.media.sound.MidiUtils;
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

    Environment env = new InMemoryEnvironment();

    /**
     * Comprueba que tras añadir un elemento en la tienda, ese esté disponible para su compra
     */
    @Test
    public void getItemTest() {
        Shop shop = env.getShop();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        shop.saveItem(raton);
        List<Item> items = shop.getItems();
        boolean found = false;
        for (Item item : items) {
            //comprueba si el item de la iteración es ratón
            //TO DO
            // si lo encuentra found = true
            if (found) {
                found = true;
            }
            assertTrue(found);
            assertFalse(!found);
        }
    }

    /**
     * Comprueba si se ha eliminado un item de la tienda al no estar disponible en el stock.
     */

    @Test
    public void removeItemTest(){
        Shop shop = env.getShop();
        Item  raton = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        shop.removeItem(101L);
        List<Item> items = shop.getItems();
        boolean found = false;
        for (Item item : items) {
            if (!found){
                found = false;
            }
        }
        assertFalse(found);
    }

    /**
     * Comprueba si al reservar un item pasa al carrito disminuyendo en la cantiad del stock
     */
    @Test
    public void reserveItemTest() throws NotAvailableItem {
        Shop shop = env.getShop();
        ShoppingCart cart = new InMemoryShoppingCart();
        Item  raton = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        shop.reserveItem(101L, 1, cart);
        cart.addItem(raton);

        assertEquals("raton", cart.getItems());
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
        ShoppingCart cart = new InMemoryShoppingCart();
        Shop shop = env.getShop();
        Order pedido = new Order();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 2);
        try {
            shop.reserveItem(101L, 2, cart );
        } catch (NotAvailableItem notAvailableItem) {
            notAvailableItem.printStackTrace();
        }
        cart.addItem(raton);
        cart.checkout();
        List<Order> orders = shop.getOrders();
        orders.add(pedido);

        boolean found = false;
        for (Order order : orders) {
            if (found){
                found = true;
            }
        }
        assertTrue(found);
        assertFalse(!found);

    }
}
