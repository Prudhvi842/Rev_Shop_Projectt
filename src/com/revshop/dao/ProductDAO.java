package com.revshop.dao;

import com.revshop.model.Product;
import com.revshop.dbutil.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {

    public java.util.List<Product> getAllProducts() {
        java.util.List<Product> list = new ArrayList<Product>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM product_1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
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
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    public java.util.List<Product> searchProducts(String keyword) {
        java.util.List<Product> list = new ArrayList<Product>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM product_1 WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ?";
            ps = conn.prepareStatement(sql);

            String key = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
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
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    public Product getProductById(int productId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM product_1 WHERE product_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setCategory(rs.getString("category"));
                p.setPrice(rs.getDouble("price"));
                p.setDiscountPrice(rs.getDouble("discount_price"));
                p.setStock(rs.getInt("stock"));
                p.setStockThreshold(rs.getInt("stock_threshold"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }
    public java.util.List<Product> getProductsByCategory(String category) {
        java.util.List<Product> list = new java.util.ArrayList<Product>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM product_1 WHERE LOWER(category) = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, category.toLowerCase());
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
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
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return list;
    }
    public boolean deleteProduct(int productId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "DELETE FROM product_1 WHERE product_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    public java.util.List<Product> getProductsBySeller(int sellerId) {
        java.util.List<Product> list = new java.util.ArrayList<Product>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM product_1 WHERE seller_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sellerId);

            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
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
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }
    public boolean updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "UPDATE product_1 SET name = ?, description = ?, category = ?, price = ?, discount_price = ?, stock = ?, stock_threshold = ? WHERE product_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setDouble(4, product.getPrice());
            ps.setDouble(5, product.getDiscountPrice());
            ps.setInt(6, product.getStock());
            ps.setInt(7, product.getStockThreshold());
            ps.setInt(8, product.getProductId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
public int addProduct(Product product) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int generatedId = 0;

    try {
        conn = DB_Connection.getConnection();
        String sql = "INSERT INTO product_1 (product_id, seller_id, name, description, category, price, discount_price, stock, stock_threshold) " +
                     "VALUES (product_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(sql, new String[] { "product_id" });

        ps.setInt(1, product.getSellerId());
        ps.setString(2, product.getName());
        ps.setString(3, product.getDescription());
        ps.setString(4, product.getCategory());
        ps.setDouble(5, product.getPrice());
        ps.setDouble(6, product.getDiscountPrice());
        ps.setInt(7, product.getStock());
        ps.setInt(8, product.getStockThreshold());

        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
        if (rs.next()) {
            generatedId = rs.getInt(1);
            product.setProductId(generatedId);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException e) {}
        try { if (ps != null) ps.close(); } catch (SQLException e) {}
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }
    return generatedId;
}


}
