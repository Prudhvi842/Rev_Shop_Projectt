package com.revshop.service;

import com.revshop.dao.FavoriteDAO;
import com.revshop.model.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class FavoriteService {

    private static final Log logger = LogFactory.getLog(FavoriteService.class);

    private FavoriteDAO favoriteDAO = new FavoriteDAO();

    public boolean addFavorite(int buyerId, int productId) {
        logger.info("Adding favorite: buyerId=" + buyerId + ", productId=" + productId);

        boolean success = favoriteDAO.addFavorite(buyerId, productId);
        if (success) {
            logger.info("Favorite added successfully: buyerId=" + buyerId + ", productId=" + productId);
        } else {
            logger.warn("Failed to add favorite: buyerId=" + buyerId + ", productId=" + productId);
        }
        return success;
    }

    public boolean removeFavorite(int buyerId, int productId) {
        logger.info("Removing favorite: buyerId=" + buyerId + ", productId=" + productId);

        boolean success = favoriteDAO.removeFavorite(buyerId, productId);
        if (success) {
            logger.info("Favorite removed successfully: buyerId=" + buyerId + ", productId=" + productId);
        } else {
            logger.warn("Failed to remove favorite: buyerId=" + buyerId + ", productId=" + productId);
        }
        return success;
    }

    public List<Product> getFavorites(int buyerId) {
        logger.info("Retrieving favorites for buyerId=" + buyerId);

        List<Product> favorites = favoriteDAO.getFavoriteProductsByBuyer(buyerId);
        if (favorites != null) {
            logger.debug("Number of favorites found: " + favorites.size());
        } else {
            logger.warn("No favorites found for buyerId=" + buyerId);
        }
        return favorites;
    }
}
