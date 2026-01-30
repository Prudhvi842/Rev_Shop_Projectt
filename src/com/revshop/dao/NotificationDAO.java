package com.revshop.dao;

// Java SQL imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Java util imports
import java.util.List;
import java.util.ArrayList;

// Application imports
import com.revshop.model.Notification;
import com.revshop.dbutil.DB_Connection;

public class NotificationDAO {

    public boolean addNotification(Notification noti) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "INSERT INTO notifications_1 (notification_id, user_id, message, is_read) "
                       + "VALUES (notification_seq.NEXTVAL, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, noti.getUserId());
            ps.setString(2, noti.getMessage());
            ps.setString(3, noti.getIsRead() == null ? "N" : noti.getIsRead());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Notification> getNotificationsByUser(int userId) {
        List<Notification> list = new ArrayList<Notification>(); // Works now because of imports
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DB_Connection.getConnection();
            String sql = "SELECT * FROM notifications_1 WHERE user_id = ? ORDER BY created_date DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setUserId(rs.getInt("user_id"));
                n.setMessage(rs.getString("message"));
                n.setCreatedDate(rs.getDate("created_date"));
                n.setIsRead(rs.getString("is_read"));
                list.add(n);
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

    public boolean markAsRead(int notificationId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DB_Connection.getConnection();
            String sql = "UPDATE notifications_1 SET is_read = 'Y' WHERE notification_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
