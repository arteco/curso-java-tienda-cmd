package com.artecoconsulting.compra.mysql;

import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class MysqlEnvironment implements Environment {

    private Database database = new Database();
    private Shop shop = new MysqlShop(database);

    @Override
    public Shop getShop() {
        return shop;
    }

    @Override
    public ShoppingCart newShoppingCart() {
        return new MysqlShoppingCart(database);
    }
}
