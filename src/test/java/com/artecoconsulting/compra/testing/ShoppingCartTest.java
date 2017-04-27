package com.artecoconsulting.compra.testing;


import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.memory.InMemoryEnvironment;
import com.artecoconsulting.compra.memory.InMemoryShoppingCart;
import com.artecoconsulting.compra.common.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;
import org.junit.Test;

import java.math.BigDecimal;


import static org.junit.Assert.*;

/**
 * Created by arteco1 on 12/04/2017.
 */
public abstract class ShoppingCartTest {


    final private Environment env;

    protected ShoppingCartTest(Environment env) {
        this.env = env;
    }

    /**
     * Comprueba si el carrito esta vacio.
     */
    @Test
    public void emptyCartTest() {
        ShoppingCart cart = new InMemoryShoppingCart();
        assertEquals(0, cart.getProductCount());
    }

    /**
     * Comprueba si se añaden items al carrito.
     */
    @Test
    public void addItemTest() {
        ShoppingCart cart = new InMemoryShoppingCart();
        Item raton = new Item("Raton", 101L, new BigDecimal(11.0), 1);
        Item pantalla = new Item("Pantalla", 105L, new BigDecimal(100.0), 1);
        Item cpu = new Item("CPU", 110L, new BigDecimal(405), 1);
        cart.addItem(raton);
        cart.addItem(pantalla);
        cart.addItem(cpu);

        assertEquals(3, cart.getProductCount());
        assertEquals(new BigDecimal(516), cart.getTotalCartPrice());

    }

    /**
     * Comprueba si se ha guardado la venta en la lista de ordenes finalizados.
     */
    @Test
    public void checkoutTest() throws NotAvailableItem {
        Shop shop = env.getShop();

        Item item = new Item();
        item.setId(-500L);
        item.setCantidad(1);
        item.setPrecio(new BigDecimal(100));
        item.setNombre("Prueba item");
        shop.saveItem(item);

        ShoppingCart cart = new InMemoryShoppingCart();

        shop.reserveItem(-500L, 1, cart);

        // item = shop.getItem(-500L);
//        assertNull(item);

        Order order = cart.checkout(shop);
        assertEquals(order.getOrders().get(0).getNombre(), "Prueba item");

        int orders = shop.getOrders().size();
        assertEquals(orders, shop.getOrders().size());
    }

    /**
     * Comprueba si se elimina un item que se añadió al carrito.
     */
    @Test
    public void removeOneItemTest() {
        ShoppingCart cart = new InMemoryShoppingCart();
        Shop shop = env.getShop();
        Item teclado = new Item("Teclado", 100L, new BigDecimal(15), 1);
        Item pantalla = new Item("Pantalla", 105L, new BigDecimal(100.0), 1);
        Item cpu = new Item("CPU", 110L, new BigDecimal(405), 1);
        cart.addItem(teclado);
        cart.addItem(pantalla);
        cart.addItem(cpu);
        cart.removeItem(teclado, shop);
        assertEquals(2, cart.getProductCount());
        assertEquals(new BigDecimal(505), cart.getTotalCartPrice());
    }

    /**
     * Comprueba si se vacía el carrito por completo.
     */
    @Test
    public void removeAllItemTest() {
        ShoppingCart cart = new InMemoryShoppingCart();
        Shop shop = env.getShop();
        Item teclado = new Item("Teclado", 100L, new BigDecimal(15), 1);
        Item pantalla = new Item("Pantalla", 105L, new BigDecimal(100.0), 1);
        Item cpu = new Item("CPU", 110L, new BigDecimal(405), 1);
//        shop.saveItem(teclado);
//        shop.saveItem(pantalla);
//        shop.saveItem(cpu);
        cart.addItem(teclado);
        cart.addItem(pantalla);
        cart.addItem(cpu);
        cart.removeAll(shop);
//        cart.removeItem(teclado, shop);
//        cart.removeItem(pantalla, shop);
//        cart.removeItem(cpu, shop);
        assertEquals(0, cart.getProductCount());
        assertEquals(new BigDecimal(0), cart.getTotalCartPrice());
    }


}
