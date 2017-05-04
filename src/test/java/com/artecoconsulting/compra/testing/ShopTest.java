package com.artecoconsulting.compra.testing;

import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.common.NotAvailableItem;
import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by arteco1 on 12/04/2017.
 */
public abstract class ShopTest {

    final private Environment env;

    protected ShopTest(Environment env) {
        this.env = env;
    }

    /**
     * Comprueba que tras añadir un elemento en la tienda, ese esté disponible para su compra
     */
    @Test
    public void saveItemTest() {
        Shop shop = env.getShop();
        Item raton = new Item(201L, "Raton", 1, new BigDecimal(11.0));
        shop.saveItem(raton);
        boolean found = false;
        for (Item item : shop.getItems()) {
            //comprueba si el item de la iteración es ratón
            // si lo encuentra found = true
            if (item.getId() != null) {
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
        Item item1 = new Item(150L, "Raton", 1, new BigDecimal(11.0));
        Item item2 = new Item(151L, "item1", 1, new BigDecimal(11.0));
        int actualCount = shop.getItems().size();
        shop.saveItem(item1);
        shop.saveItem(item2);
        shop.removeItem(150L);
        shop.removeItem(151L);
        assertEquals(actualCount, shop.getItems().size());
    }

    /**
     * Comprueba si al reservar un item pasa al carrito disminuyendo en la cantiad del stock
     */
    @Test
    public void reserveItemTest() throws NotAvailableItem {
        Shop shop = env.getShop();
        ShoppingCart cart = env.newShoppingCart();
        Item raton = new Item(202L, "Raton", 5, new BigDecimal(11.0));
        //añadimos el item al stock
        shop.saveItem(raton);
        shop.reserveItem(202L, 1, cart);
        assertEquals(1, cart.getProductCount());
    }

    /**
     * Comprueba la cantidad total que hay en stock de un item.
     */
    @Test
    public void getTotalQuantityTest() {
        Shop shop = env.getShop();
        Item raton = new Item(203L, "Raton", 50, new BigDecimal(11.0));
        shop.saveItem(raton);
        assertEquals(50, shop.getTotalQuantity(203L));
    }

    /**
     * Comprueba si se ha guardado la venta en la lista de ordenes finalizados.
     */
    @Test
    public void getOrdersTest() throws NotAvailableItem {
        // inicializaciones
        ShoppingCart cart = env.newShoppingCart();
        ShoppingCart cart1 = env.newShoppingCart();

        Shop shop = env.getShop();
        Item raton = new Item(204L, "Raton", 2, new BigDecimal(11.0));
        Item item1 = new Item(205L, "item1", 2, new BigDecimal(11.0));
        //añadimos el item al stock
        shop.saveItem(raton);
        shop.saveItem(item1);

        // compramos el item
        shop.reserveItem(204L, 1, cart);
        shop.reserveItem(205L, 2, cart1);

        int numOrders = shop.getOrders().size();

        cart.checkout(shop);
        cart1.checkout(shop);

        assertEquals(numOrders + 2, shop.getOrders().size());
    }
}
