package com.revshop.dao;

import com.revshop.model.Review;
import com.revshop.dbutil.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewDAO {

    public boolean addReview(Review review) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "INSERT INTO product_reviews (review_id, buyer_id, product_id, rating, review_text, review_date) VALUES (NULL, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, review.getBuyerId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());
            ps.setDate(5, new java.sql.Date(review.getReviewDate().getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public java.util.List<Review> getReviewsByProduct(int productId) {
        java.util.List<Review> list = new java.util.ArrayList<Review>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM product_reviews WHERE product_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, productId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setBuyerId(rs.getInt("buyer_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setRating(rs.getInt("rating"));
                review.setReviewText(rs.getString("review_text"));
                review.setReviewDate(rs.getDate("review_date"));
                list.add(review);
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
}
