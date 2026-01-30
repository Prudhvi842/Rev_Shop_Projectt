package com.revshop.dao;

import com.revshop.dbutil.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoriteDAO {

    public boolean addFavorite(int buyerId, int productId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "INSERT INTO favorite_1 (favorite_id, buyer_id, product_id) VALUES (NULL, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    public boolean removeFavorite(int buyerId, int productId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "DELETE FROM favorite_1 WHERE buyer_id = ? AND product_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, buyerId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch(Exception e){}
            try { if (conn != null) conn.close(); } catch(Exception e){}
        }
    }
    public java.util.List<com.revshop.model.Product> getFavoriteProductsByBuyer(int buyerId) {
        java.util.List<com.revshop.model.Product> list =
            new java.util.ArrayList<com.revshop.model.Product>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT p.* FROM product_1 p "
                       + "JOIN favorite_1 f ON p.product_id = f.product_id "
                       + "WHERE f.buyer_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, buyerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                com.revshop.model.Product p = new com.revshop.model.Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setPrice(rs.getDouble("price"));
                p.setDiscountPrice(rs.getDouble("discount_price"));
                p.setStock(rs.getInt("stock"));
                p.setStockThreshold(rs.getInt("stock_threshold"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e){}
            try { if (ps != null) ps.close(); } catch(Exception e){}
            try { if (conn != null) conn.close(); } catch(Exception e){}
        }
        return list;
    }

}
