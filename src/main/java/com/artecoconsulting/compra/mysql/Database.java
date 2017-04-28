package com.artecoconsulting.compra.mysql;

import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arteco1 on 27/04/2017.
 */
public class Database {

    private static Connection conn;

    static {
        try {
            // inicialización de la conexión con la base de datos
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost/shop?" +
                    "user=root&password=arteco");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //Implementar operaciones del CRUD para items, y orders

    public List<Item> getItems() {
        try {
            List<Item> items = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("select * from items order by id asc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = parseItem(rs);
                items.add(item);
            }
            rs.close();
            ps.close();
            return items;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Item getItem(Long id) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("select * from items where id = ? ");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return parseItem(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Item parseItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        // hacer todos los setters de item
        item.setId(rs.getLong("id"));
        item.setNombre(rs.getString("nombre"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setPrecio(rs.getBigDecimal("precio"));
        return item;
    }

    public void saveItem(Item item) {
        //insert or update, en función de si viene id = null o no
        if (item.getId()==null){
            item.setId((long) getItems().size());
        }
        if (getItem(item.getId())== null){
            insertItem(item);
        }else {
            updateItem(item);
        }
    }

    private void insertItem(Item item) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO items(id,precio,nombre,cantidad) values(?,?,?,?)");
            ps.setLong(1, item.getId());
            ps.setDouble(2, item.getPrecio().doubleValue());
            ps.setString(3, item.getNombre());
            ps.setInt(4, item.getCantidad());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateItem(Item item) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(" update items set nombre = ?, cantidad = ?, precio = ? where id = ? ");
            ps.setString(1, item.getNombre());
            ps.setInt(2, item.getCantidad());
            ps.setDouble(3, item.getPrecio().doubleValue());
            ps.setLong(4, item.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(Item item) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM items where id = ?"); //SQL Injection id = +item.getId()
            ps.setLong(1, item.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO


    public void saveOrder(Order order) {
        PreparedStatement ps = null;
        try {
            if (order.getIdOrder()==null){
                order.setIdOrder((long) getOrders().size());
            }
            for (Item item : order.getOrders()) {
                ps = conn.prepareStatement("INSERT INTO orders(idOrder, idItem, cantidad) values(?,?,?)");
                ps.setLong(1, order.getIdOrder());
                ps.setLong(2, item.getId());
                ps.setInt(3, item.getCantidad());
                ps.executeUpdate();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public long countItems() {
        PreparedStatement ps = null;
        int cantidad = 0;
        try {
            ps = conn.prepareStatement("Select count(cantidad) from items where id = ?");
            ps.setInt(1, cantidad);
            ResultSet rs = ps.executeQuery();
            for (Item item : getItems()){
                cantidad ++; //= item.getCantidad();
            }
//            if (rs.next()){
//                cantidad ++;
//                return parseItem(rs).getCantidad().intValue();
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cantidad;

    }

    public List<Order> getOrders() {
        try {
            List<Order> orders = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("select * from orders ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = parseOrder(rs);
                orders.add(order);
            }
            rs.close();
            ps.close();
            return orders;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private Order parseOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
       // order.setIdOrder(rs.getLong("idOrder"));
        return order;
    }

    public void saveCartItem(Long cartId, Item item) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO cartItem(idCart, idItem, cantidad) values(?,?,?)");
            ps.setLong(1, cartId);
            ps.setLong(2, item.getId());
            ps.setInt(3, item.getCantidad());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getCartItems(Long cartId) {
        try {
            List<Item> cartItems = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("select * from cartItem ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = parseCartItem(rs);
                cartItems.add(item);
            }
            rs.close();
            ps.close();
            return cartItems;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Item parseCartItem(ResultSet rs) throws SQLException{
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setNombre(rs.getString("nombre"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setPrecio(rs.getBigDecimal("precio"));
        return item;

    }

    public void removeAllCartItems(Long cartId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE * FROM cartItem");
            ps.setLong(1, cartId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCartItem(Long cartId, Item item) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM items where id = ?");
            ps.setLong(1, item.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
