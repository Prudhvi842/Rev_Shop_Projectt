package com.revshop.dao;

import com.revshop.model.Order;
import com.revshop.model.CartItem;
import com.revshop.dbutil.DB_Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int saveOrderReturnId(Order order, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders_1 (order_id, buyer_id, order_date, total_amount, status, shipping_address, billing_address) "
                   + "VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"order_id"});
        ps.setInt(1, order.getBuyerId());
        ps.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
        ps.setDouble(3, order.getTotalAmount());
        ps.setString(4, order.getStatus());
        ps.setString(5, order.getShippingAddress());
        ps.setString(6, order.getBillingAddress());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        int orderId = 0;
        if (rs.next()) {
            orderId = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return orderId;
    }

    public List<CartItem> getCartByBuyer(int buyerId, Connection conn) throws SQLException {
        List<CartItem> list = new ArrayList<CartItem>();
        String sql = "SELECT * FROM cart_1 WHERE buyer_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, buyerId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CartItem ci = new CartItem();
            ci.setCartId(rs.getInt("cart_id"));
            ci.setBuyerId(rs.getInt("buyer_id"));
            ci.setProductId(rs.getInt("product_id"));
            ci.setQuantity(rs.getInt("quantity"));
            list.add(ci);
        }
        rs.close();
        ps.close();
        return list;
    }

    public boolean saveOrderItem(int orderId, int productId, int qty, double price, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_item_1 (order_item_id, order_id, product_id, quantity, price) "
                   + "VALUES (order_item_seq.NEXTVAL, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, orderId);
        ps.setInt(2, productId);
        ps.setInt(3, qty);
        ps.setDouble(4, price);
        boolean res = ps.executeUpdate() > 0;
        ps.close();
        return res;
    }

    public boolean updateProductStock(int productId, int newStock, Connection conn) throws SQLException {
        String sql = "UPDATE product_1 SET stock = ? WHERE product_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newStock);
        ps.setInt(2, productId);
        boolean res = ps.executeUpdate() > 0;
        ps.close();
        return res;
    }

    public List<Integer> getSellersForOrder(int orderId) {
        List<Integer> sellers = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT DISTINCT p.seller_id FROM order_item_1 oi "
                       + "JOIN product_1 p ON oi.product_id = p.product_id "
                       + "WHERE oi.order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                sellers.add(rs.getInt("seller_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return sellers;
    }

    public java.util.List<Order> getOrdersByBuyer(int buyerId) {
        java.util.List<Order> list = new ArrayList<Order>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM orders_1 WHERE buyer_id = ? ORDER BY order_date DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, buyerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setBuyerId(rs.getInt("buyer_id"));
                o.setOrderDate(rs.getDate("order_date"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setShippingAddress(rs.getString("shipping_address"));
                o.setBillingAddress(rs.getString("billing_address"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return list;
    }

    public java.util.List<Order> getOrdersForSeller(int sellerId) {
        java.util.List<Order> list = new ArrayList<Order>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT DISTINCT o.* FROM orders_1 o "
                       + "JOIN order_item_1 oi ON o.order_id = oi.order_id "
                       + "JOIN product_1 p ON oi.product_id = p.product_id "
                       + "WHERE p.seller_id = ? ORDER BY o.order_date DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sellerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setBuyerId(rs.getInt("buyer_id"));
                o.setOrderDate(rs.getDate("order_date"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setStatus(rs.getString("status"));
                o.setShippingAddress(rs.getString("shipping_address"));
                o.setBillingAddress(rs.getString("billing_address"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return list;
    }
}
