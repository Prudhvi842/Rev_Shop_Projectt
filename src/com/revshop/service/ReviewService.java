package com.revshop.service;

import com.revshop.dao.ReviewDAO;
import com.revshop.model.Review;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

public class ReviewService {

    private static final Log logger = LogFactory.getLog(ReviewService.class);
    private ReviewDAO reviewDAO = new ReviewDAO();

    public boolean addReview(int buyerId, int productId, int rating, String reviewText) {
        logger.info("Attempting to add review: buyerId=" + buyerId + ", productId=" + productId
                + ", rating=" + rating);

        if (rating < 1 || rating > 5) {
            logger.warn("Invalid rating value (" + rating + ") for productId=" + productId);
            System.out.println("Rating must be between 1 and 5!");
            return false;
        }

        Review review = new Review();
        review.setBuyerId(buyerId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setReviewText(reviewText);
        review.setReviewDate(new Date());

        boolean success = reviewDAO.addReview(review);
        if (success) {
            logger.info("Review added successfully for productId=" + productId + " by buyerId=" + buyerId);
        } else {
            logger.error("Failed to add review for productId=" + productId + " by buyerId=" + buyerId);
        }
        return success;
    }

    public List<Review> getReviewsForProduct(int productId) {
        logger.info("Fetching reviews for productId=" + productId);

        List<Review> reviews = reviewDAO.getReviewsByProduct(productId);
        if (reviews == null || reviews.isEmpty()) {
            logger.debug("No reviews found for productId=" + productId);
        } else {
            logger.debug("Found " + reviews.size() + " reviews for productId=" + productId);
        }
        return reviews;
    }
}
