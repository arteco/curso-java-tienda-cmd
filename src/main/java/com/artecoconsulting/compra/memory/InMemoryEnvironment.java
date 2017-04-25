package com.artecoconsulting.compra.memory;

import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by arteco1 on 20/04/2017.
 */
public class InMemoryEnvironment implements Environment {

    private Shop shop = new InMemoryShop();
    private ShoppingCart cart = new InMemoryShoppingCart();

    @Override
    public Shop getShop() {
        return shop;
    }

    public ShoppingCart getCart() {
        return cart;
    }
}
