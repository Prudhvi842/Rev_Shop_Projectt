package com.revshop.service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.revshop.model.User;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testLoginValid() {
        User user = userService.loginUser("d@gmail.com", "12345");
        assertNotNull("Valid login should return a user", user);
    }

    @Test
    public void testLoginInvalid() {
        User user = userService.loginUser("notfound@xyz.com", "wrongpass");
        assertNull("Invalid login should return null", user);
    }
}
