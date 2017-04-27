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
            ps = conn.prepareStatement("INSERT INTO orders where id != NULL");
            ps.setLong(1, order);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public long countItems() {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("Select cantidad from items where id != NULL");
            ps.setInt(int);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Order> getOrders() {
        try {
            List<Order> orders = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("select * from orders order by id asc");
            ResultSet rs = ps.executeQuery();
            return orders;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public void saveCartItem(Long cartId, Item item) {
    }

    public List<Item> getCartItems(Long cartId) {
    }

    public void removeAllCartItems(Long cartId) {
    }

    public void removeCartItem(Long cartId, Item item) {
    }
}
