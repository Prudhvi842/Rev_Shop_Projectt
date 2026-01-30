package com.revshop.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.revshop.dao.ReviewDAO;
import com.revshop.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceTest {

    private ReviewService rs;
    private ReviewDAO reviewDAOMock;

    @Before
    public void setUp() {
        rs = new ReviewService();

        // create mock
        reviewDAOMock = mock(ReviewDAO.class);

        // inject mock into the private field
        try {
            java.lang.reflect.Field field = ReviewService.class.getDeclaredField("reviewDAO");
            field.setAccessible(true);
            field.set(rs, reviewDAOMock);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to inject mock DAO");
        }
    }

    @Test
    public void testAddAndGetReviews() {

        // set up mock behavior for addReview
        when(reviewDAOMock.addReview(any(Review.class))).thenReturn(true);

        boolean added = rs.addReview(1, 1, 5, "JUnit OK");
        assertTrue("addReview should return true", added);

        // set up mock behavior for getReviewsByProduct
        List<Review> fakeReviews = new ArrayList<Review>();
        fakeReviews.add(new Review());
        when(reviewDAOMock.getReviewsByProduct(1)).thenReturn(fakeReviews);

        List<Review> result = rs.getReviewsForProduct(1);
        assertNotNull("Review list should not be null", result);
        assertEquals("Should return exactly 1 review", 1, result.size());
    }

    @Test
    public void testAddReviewInvalidRating() {
        // no need to stub DAO for invalid rating
        boolean added = rs.addReview(1, 1, 6, "Bad rating");
        assertFalse("Rating above 5 should be false", added);
    }
}
