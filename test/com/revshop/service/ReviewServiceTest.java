package com.revshop.service;

import static org.junit.Assert.*;
import org.junit.*;
import com.revshop.service.ReviewService;

public class ReviewServiceTest {

    private ReviewService rs;

    @Before
    public void setUp() {
        rs = new ReviewService();
    }

    @Test
    public void testAddAndGetReviews() {
        assertTrue(rs.addReview(1,1,5,"JUnit OK"));
        assertNotNull(rs.getReviewsForProduct(1));
    }
}

