package com.revshop.service;

import static org.junit.Assert.*;
import org.junit.*;
import com.revshop.service.OrderService;

public class OrderServiceTest {

    private OrderService os;

    @Before
    public void setUp() {
        os = new OrderService();
    }

    @Test
    public void testEmptyCartCheckout() {
        assertFalse("Checkout should fail for empty cart", os.placeOrderTransactional(99999, "addr1", "addr2"));
    }
}

