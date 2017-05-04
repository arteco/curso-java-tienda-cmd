package com.artecoconsulting.compra.mysql;

import com.artecoconsulting.compra.model.Item;
import com.artecoconsulting.compra.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arteco1 on 27/04/2017.
 */
@Slf4j
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

    /**
     * Se piden los items que hay en el shop ordenadas por su id ascendente.
     * @return items
     */
    public List<Item> getItems() {
        try {
            List<Item> items = new ArrayList<>();
            String sql = "select * from items order by id asc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = parseItem(rs);
                items.add(item);
            }
            log.info(sql);

            rs.close();
            ps.close();
            return items;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Se pide un item pasandole su id.
     * @param id de item
     * @return si no lo encuentra, devuelve null.
     */
    public Item getItem(Long id) {
        PreparedStatement ps = null;
        try {
            String sql ="select * from items where id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return parseItem(rs);

            }
            log.info(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Setters de item
     * @param rs
     * @return item
     * @throws SQLException
     */
    private Item parseItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        // hacer todos los setters de item
        item.setId(rs.getLong("id"));
        item.setNombre(rs.getString("nombre"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setPrecio(rs.getBigDecimal("precio"));
        return item;
    }

    /**
     * Guarda el item en shop.
     * @param item
     */
    public void saveItem(Item item) {
        //insert or update, en función de si viene id = null o no
        if (item.getId() == null) {
            item.setId((long) getItems().size());
        }
        if (getItem(item.getId()) == null) {
            insertItem(item);
        } else {
            updateItem(item);
        }
    }

    /**
     * Si no existe el item el el shop, asignamos un id al item y guradamos en shop.
     * @param item
     */
    private void insertItem(Item item) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO items(id,precio,nombre,cantidad) values(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, item.getId());
            ps.setDouble(2, item.getPrecio().doubleValue());
            ps.setString(3, item.getNombre());
            ps.setInt(4, item.getCantidad());
            ps.executeUpdate();
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Si item(id) existe en shop, actualizamos los datos del item.
     * @param item id del item
     */
    private void updateItem(Item item) {
        PreparedStatement ps = null;
        try {
            String sql = " update items set nombre = ?, cantidad = ?, precio = ? where id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, item.getNombre());
            ps.setInt(2, item.getCantidad());
            ps.setDouble(3, item.getPrecio().doubleValue());
            ps.setLong(4, item.getId());
            ps.executeUpdate();
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Eliminamos un item pasandole el id Item.
     * @param item
     */
    public void removeItem(Item item) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM items where id = ?";
            ps = conn.prepareStatement(sql); //SQL Injection id = +item.getId()
            ps.setLong(1, item.getId());
            ps.executeUpdate();
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guardamos en orders cada venta finalizada al hacer el checkout de un carrito.
     * @param order
     */
    public void saveOrder(Order order) {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO orders(idOrder, idItem, cantidad, precio) values(?,?,?,?)";
            if (order.getIdOrder() == null) {
                order.setIdOrder((long) getOrders().size() + 1);
            }
            for (Item item : order.getOrders()) {
                ps = conn.prepareStatement(sql);
                ps.setLong(1, order.getIdOrder());
                ps.setLong(2, item.getId());
                ps.setInt(3, item.getCantidad());
                ps.setDouble(4, item.getPrecio().doubleValue());
                ps.executeUpdate();
                ps.close();
                log.info(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consultar la cantidad de un item por su id.
     * @return cantidad de item
     */

    public long countItems() {
        PreparedStatement ps = null;
        int cantidad = 0;
        try {
            String sql = "Select count(cantidad) from items where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ResultSet rs = ps.executeQuery();
            for (Item item : getItems()) {
                cantidad++; //= item.getCantidad();
            }
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cantidad;

    }

    /**
     * Devuelve todas las ventas finalizadas.
     * @return lista orders.
     */
    public List<Order> getOrders() {
        try {
            // TODO: utilizar un HashMap<Long,Order>
            String sql ="select * from orders order by idOrder asc";
            List<Order> orders = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Long lastOrderId = null;
            Order order = null;
            while (rs.next()) {
                long orderId = rs.getLong("idOrder");
                if (lastOrderId == null || orderId != lastOrderId) {
                    order = new Order();
                    order.setIdOrder(orderId);
                    order.setOrders(new ArrayList<Item>());
                    orders.add(order);
                }
                //añadir items dentro de order
                Item item = new Item();
                item.setId(rs.getLong("idItem"));
                item.setCantidad(rs.getInt("cantidad"));
                order.getOrders().add(item);
            }
            rs.close();
            ps.close();
            log.info(sql);
            return orders;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Añade un item al carrito, bloqueando ese item del shop para comprar.
     * @param cartId id de carrito.
     * @param item item a comprar.
     * @param cantidad de cada item.
     */
    public void saveCartItem(Long cartId, Item item, int cantidad) {
        PreparedStatement ps = null;
        try {
            String sql ="INSERT INTO cartItem(idCart, idItem, nombre, cantidad, precio) values(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, cartId);
            ps.setLong(2, item.getId());
            ps.setString(3, item.getNombre());
            ps.setInt(4, cantidad);
            ps.setDouble(5, item.getPrecio().doubleValue());
            ps.executeUpdate();
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtenemos todos los items que hay en carrito.
     * @param cartId id de cada carrito.
     * @return los items del carrito.
     */
    public List<Item> getCartItems(Long cartId) {
        try {
            String sql ="select * from cartItem where idCart = ? ";
            List<Item> cartItems = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = parseCartItem(rs);
                cartItems.add(item);
            }
            rs.close();
            ps.close();
            log.info(sql);
            return cartItems;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Setters de item en el carrito.
     * @param rs
     * @return item
     * @throws SQLException
     */
    private Item parseCartItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getLong("idItem"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setPrecio(rs.getBigDecimal("precio"));
        item.setNombre(rs.getString("nombre"));
        return item;

    }

    /**
     * Borrar todos los items del carrito.
     * @param cartId id del carrito.
     */
    public void removeAllCartItems(Long cartId) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM cartItem where idCart = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, cartId);
            ps.executeUpdate();
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Borrar un item del carrito.
     * @param cartId id del carrito
     * @param item id de item.
     */
    public void removeCartItem(Long cartId, Item item) {
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM cartItem where idCart = ? and idItem = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, cartId);
            ps.setLong(2, item.getId());
            ps.executeUpdate();
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el id maximimo que hay asignado para los carritos, para asignarle +1 al siguiente carrito
     * @return id maximo.
     */
    public long getMaxCartId() {
        long res = 0;
        PreparedStatement ps = null;
        try {
            String sql = "select max(idCart) FROM cartItem";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                res = rs.getLong(1);
            }
            ps.close();
            log.info(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
