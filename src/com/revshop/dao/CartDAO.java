package com.revshop.dao;

import com.revshop.model.CartItem;
import com.revshop.dbutil.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO {

    public boolean addToCart(CartItem item) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "INSERT INTO cart_1 (cart_id, buyer_id, product_id, quantity) VALUES (NULL, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, item.getBuyerId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public java.util.List<CartItem> getCartByBuyer(int buyerId) {
        java.util.List<CartItem> list = new java.util.ArrayList<CartItem>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM cart_1 WHERE buyer_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, buyerId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartId(rs.getInt("cart_id"));
                item.setBuyerId(rs.getInt("buyer_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    public boolean removeFromCart(int cartId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "DELETE FROM cart_1 WHERE cart_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close(); // <-- fixed here
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean clearCartByBuyer(int buyerId, Connection conn) throws SQLException {
        String sql = "DELETE FROM cart_1 WHERE buyer_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, buyerId);
        boolean result = ps.executeUpdate() > 0;
        ps.close();
        return result;
    }
    public boolean clearCartByBuyer(int buyerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "DELETE FROM cart_1 WHERE buyer_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, buyerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch(SQLException e){}
            try { if (conn != null) conn.close(); } catch(SQLException e){}
        }
    }


}
