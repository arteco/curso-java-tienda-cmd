package com.artecoconsulting.compra.memory;

import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import com.artecoconsulting.compra.model.Shop;
import com.artecoconsulting.compra.model.ShoppingCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arteco1 on 24/04/2017.
 */
public class InMemoryShoppingCart implements ShoppingCart {

    static long globalId = 0;

    private long id = globalId++;
    private List<Item> items = new ArrayList<>();


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean addItem(Item item) {
        if (item != null) {
            items.add(item);
            return true;
        }
        return false;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

    @Override
    public int getProductCount() {
        int totalCantidad = 0;
        for (Item item : items) {
            totalCantidad += item.getCantidad();
        }
        return totalCantidad;
    }

    @Override
    public Order checkout(Shop shop) {
        Order order = new Order(new ArrayList<>(items));
        items.clear();
        shop.addOrder(order);
        return order;
    }

    @Override
    public void removeItem(Item item, Shop shop) {
        items.remove(item);
        Item dbItem = shop.getItem(item.getId());
        if (dbItem != null) {
            dbItem.setCantidad(item.getCantidad() + dbItem.getCantidad());
        }
    }

    @Override
    public void removeAll(Shop shop) {
        List<Item> items = new ArrayList<>(getItems());
        for (Item item : items) {
            removeItem(item, shop);
        }
    }

    @Override
    public BigDecimal getTotalCartPrice() {
        BigDecimal costeTotal = new BigDecimal(0);
        for (Item item : getItems()) {
            costeTotal = costeTotal.add(item.getPrecio())
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
        }
        return costeTotal;
    }
}
