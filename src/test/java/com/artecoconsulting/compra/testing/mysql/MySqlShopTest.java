package com.artecoconsulting.compra.testing.mysql;

import com.artecoconsulting.compra.mysql.MysqlEnvironment;
import com.artecoconsulting.compra.testing.ShopTest;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class MySqlShopTest extends ShopTest {
    public MySqlShopTest() {
        super(new MysqlEnvironment());
    }
}
