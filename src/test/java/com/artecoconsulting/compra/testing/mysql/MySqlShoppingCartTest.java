package com.artecoconsulting.compra.testing.mysql;

import com.artecoconsulting.compra.memory.InMemoryEnvironment;
import com.artecoconsulting.compra.mysql.MysqlEnvironment;
import com.artecoconsulting.compra.testing.ShoppingCartTest;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class MySqlShoppingCartTest extends ShoppingCartTest {

    protected MySqlShoppingCartTest() {
        super(new MysqlEnvironment());
    }
}
