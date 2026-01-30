package com.revshop.service;

import static org.junit.Assert.*;
import org.junit.*;
import com.revshop.service.NotificationService;

public class NotificationServiceTest {

    private NotificationService ns;

    @Before
    public void setUp() {
        ns = new NotificationService();
    }

    @Test
    public void testAddAndReadNotifications() {
        assertTrue("Should add a notification", ns.addNotification(1, "Test"));
        java.util.List<com.revshop.model.Notification> list = ns.getNotifications(1);
        assertNotNull(list);
    }
}
