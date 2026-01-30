package com.revshop.dao;

import com.revshop.model.User;
import com.revshop.dbutil.DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

	public boolean addUser(User user) {
	    Connection conn = null;
	    PreparedStatement ps = null;

	    try {
	        conn = DB_Connection.getConnection();
	        String sql = "INSERT INTO user_1 (user_id, name, email, password, role, security_question, security_answer, password_hint) "
	                   + "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";
	        ps = conn.prepareStatement(sql);

	        ps.setString(1, user.getName());
	        ps.setString(2, user.getEmail());
	        ps.setString(3, user.getPassword());
	        ps.setString(4, user.getRole());

	        // Safety: handle missing security fields
	        ps.setString(5, user.getSecurityQuestion() != null ? user.getSecurityQuestion() : "");
	        ps.setString(6, user.getSecurityAnswer() != null ? user.getSecurityAnswer() : "");
	        ps.setString(7, user.getPasswordHint() != null ? user.getPasswordHint() : "");

	        return ps.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;

	    } finally {
	        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
	    }
	}

    public User getUserByEmailAndPassword(String email, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM user_1 WHERE email = ? AND password = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setPassword(rs.getString("password"));
                return user;
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
    public boolean updatePassword(int userId, String newPassword) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "UPDATE user_1 SET password = ? WHERE user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch(SQLException e){}
            try { if (conn != null) conn.close(); } catch(SQLException e){}
        }
    }
    public User getUserById(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM user_1 WHERE user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch(SQLException e){}
            try { if (ps != null) ps.close(); } catch(SQLException e){}
            try { if (conn != null) conn.close(); } catch(SQLException e){}
        }
        return null;
    }

    public User getUserByEmail(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM user_1 WHERE email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setSecurityQuestion(rs.getString("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
                user.setPasswordHint(rs.getString("password_hint"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e){}
            try { if (ps != null) ps.close(); } catch(Exception e){}
            try { if (conn != null) conn.close(); } catch(Exception e){}
        }
        return null;
    }

}
