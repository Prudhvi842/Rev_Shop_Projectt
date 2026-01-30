package com.revshop.service;

import com.revshop.dao.NotificationDAO;
import com.revshop.model.Notification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class NotificationService {

    private static final Log logger = LogFactory.getLog(NotificationService.class);
    private NotificationDAO notificationDAO = new NotificationDAO();

    // Add a new persistent notification
    public boolean addNotification(int userId, String message) {
        logger.info("Adding notification for userId=" + userId + " message=\"" + message + "\"");

        Notification n = new Notification();
        n.setUserId(userId);
        n.setMessage(message);
        n.setIsRead("N");

        boolean success = notificationDAO.addNotification(n);
        if (success) {
            logger.info("Notification added successfully for userId=" + userId);
        } else {
            logger.error("Failed to add notification for userId=" + userId);
        }
        return success;
    }

    public List<Notification> getNotifications(int userId) {
        logger.info("Retrieving notifications for userId=" + userId);

        List<Notification> list = notificationDAO.getNotificationsByUser(userId);
        if (list != null) {
            logger.debug("Found " + list.size() + " notifications for userId=" + userId);
        } else {
            logger.warn("No notifications list returned for userId=" + userId);
        }
        return list;
    }

    public boolean markNotificationAsRead(int notificationId) {
        logger.info("Marking notification as read: notificationId=" + notificationId);
        boolean success = notificationDAO.markAsRead(notificationId);
        if (success) {
            logger.info("Marked notificationId=" + notificationId + " as read");
        } else {
            logger.warn("Failed to mark notificationId=" + notificationId + " as read");
        }
        return success;
    }

    // Console formatting
    public void printNotifications(List<Notification> list) {
        logger.info("Printing notifications list of size=" + (list == null ? 0 : list.size()));

        System.out.println("\n--- Notifications ---");
        if (list == null || list.isEmpty()) {
            System.out.println("No notifications found.");
            logger.info("No notifications to display");
            return;
        }

        for (Notification n : list) {
            String status = n.getIsRead().equalsIgnoreCase("Y") ? "Read" : "New";
            System.out.println(n.getNotificationId() + " | " + status + " | " + n.getMessage());

            logger.debug("Notification displayed -- id=" + n.getNotificationId() +
                ", status=" + status + ", message=\"" + n.getMessage() + "\"");
        }
    }
}
