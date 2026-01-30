package com.revshop.service;

import static org.junit.Assert.*;
import org.junit.*;
import com.revshop.service.FavoriteService;

public class FavoriteServiceTest {

    private FavoriteService fav;

    @Before
    public void setUp() {
        fav = new FavoriteService();
    }

    @Test
    public void testAddAndRemoveFavorite() {
        assertTrue(fav.addFavorite(1,1));
        assertTrue(fav.removeFavorite(1,1));
    }
}

