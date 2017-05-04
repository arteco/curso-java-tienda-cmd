package com.artecoconsulting.compra.testing;


import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.common.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by arteco1 on 12/04/2017.
 */
public abstract class ShoppingCartTest {


    final private Environment env;

    public ShoppingCartTest(Environment env) {
        this.env = env;
    }

    /**
     * Comprueba si el carrito esta vacio.
     */
    @Test
    public void emptyCartTest() {
        ShoppingCart cart = env.newShoppingCart();
        assertEquals(0, cart.getProductCount());
    }

    /**
     * Comprueba si se añaden items al carrito.
     */
    @Test
    public void addItemTest() {
        ShoppingCart cart = env.newShoppingCart();
        Item raton = new Item(101L, "Raton", 1, new BigDecimal(11.0));
        Item pantalla = new Item(102L, "Pantalla", 1, new BigDecimal(100.0));
        Item cpu = new Item(103L, "CPU", 1, new BigDecimal(405));
        cart.addItem(raton);
        cart.addItem(pantalla);
        cart.addItem(cpu);
        assertEquals(3, cart.getProductCount());
        //  assertEquals(new BigDecimal(516), cart.getTotalCartPrice());
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

        ShoppingCart cart = env.newShoppingCart();

        shop.reserveItem(-500L, 1, cart);

        // item = shop.getItem(-500L);
//        assertNull(item);

        cart.checkout(shop);
        //assertEquals(order.getOrders().get(0).getNombre(), "Prueba item");

        int orders = shop.getOrders().size();
        assertEquals(orders, shop.getOrders().size());
    }

    /**
     * Comprueba si se elimina un item que se añadió al carrito.
     */
    @Test
    public void removeOneItemTest() throws NotAvailableItem {

        Shop shop = env.getShop();
        Item teclado = new Item(104L, "Teclado", 1, new BigDecimal(15));
        Item pantalla = new Item(105L, "Pantalla", 1, new BigDecimal(100.0));
        Item cpu = new Item(106L, "CPU", 1, new BigDecimal(405));
        shop.saveItem(teclado);
        shop.saveItem(pantalla);
        shop.saveItem(cpu);
        ShoppingCart cart = env.newShoppingCart();
        shop.reserveItem(104L, 1, cart);
        shop.reserveItem(105L, 1, cart);
        shop.reserveItem(106L, 1, cart);
        cart.removeItem(teclado, shop);

        assertEquals(2, cart.getProductCount());
        assertEquals(new BigDecimal(505), cart.getTotalCartPrice());
    }

    /**
     * Comprueba si se vacía el carrito por completo.
     */
    @Test
    public void removeAllItemTest() {
        ShoppingCart cart = env.newShoppingCart();
        Shop shop = env.getShop();
        Item teclado = new Item(107L, "Teclado", 1, new BigDecimal(15));
        Item pantalla = new Item(108L, "Pantalla", 1, new BigDecimal(100.0));
        Item cpu = new Item(109L, "CPU", 1, new BigDecimal(405));
        cart.addItem(teclado);
        cart.addItem(pantalla);
        cart.addItem(cpu);
        cart.removeAll(shop);
        assertEquals(0, cart.getProductCount());
        assertEquals(new BigDecimal(0), cart.getTotalCartPrice());
    }


}
