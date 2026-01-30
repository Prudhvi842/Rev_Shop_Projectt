package com.revshop.service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CartServiceTest {

    private CartService cartService;

    @Before
    public void setUp() {
        cartService = new CartService();
    }

    @Test
    public void testEmptyCartTotal() {
        double total = cartService.calculateCartTotal(-1);  // assume invalid buyer
        assertEquals(0.0, total, 0.001);
    }
}
