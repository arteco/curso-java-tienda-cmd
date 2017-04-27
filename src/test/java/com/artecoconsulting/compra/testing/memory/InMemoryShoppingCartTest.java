package com.artecoconsulting.compra.testing.memory;

import com.artecoconsulting.compra.Environment;
import com.artecoconsulting.compra.memory.InMemoryEnvironment;
import com.artecoconsulting.compra.testing.ShoppingCartTest;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class InMemoryShoppingCartTest extends ShoppingCartTest {
    protected InMemoryShoppingCartTest() {
        super(new InMemoryEnvironment());
    }
}
