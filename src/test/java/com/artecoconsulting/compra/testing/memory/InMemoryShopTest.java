package com.artecoconsulting.compra.testing.memory;

import com.artecoconsulting.compra.memory.InMemoryEnvironment;
import com.artecoconsulting.compra.testing.ShopTest;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class InMemoryShopTest extends ShopTest {

    public InMemoryShopTest() {
        super(new InMemoryEnvironment());
    }
}
