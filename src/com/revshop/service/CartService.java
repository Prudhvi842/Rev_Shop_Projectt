package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.model.CartItem;
import com.revshop.model.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class CartService {

    private static final Log logger = LogFactory.getLog(CartService.class);

    private CartDAO cartDAO = new CartDAO();

    public boolean addToCart(int buyerId, int productId, int qty) {
        logger.info("Adding to cart - buyerId: " + buyerId + ", productId: " + productId + ", qty: " + qty);

        if (qty <= 0) {
            logger.warn("Invalid quantity (" + qty + ") for buyerId: " + buyerId + ", productId: " + productId);
            return false;
        }

        CartItem item = new CartItem();
        item.setBuyerId(buyerId);
        item.setProductId(productId);
        item.setQuantity(qty);

        boolean success = cartDAO.addToCart(item);
        if (success) {
            logger.info("Added to cart successfully - cartId: " + item.getCartId());
        } else {
            logger.error("Failed to add to cart - buyerId: " + buyerId + ", productId: " + productId);
        }
        return success;
    }

    public List<CartItem> viewCart(int buyerId) {
        logger.info("Viewing cart for buyerId: " + buyerId);
        List<CartItem> items = cartDAO.getCartByBuyer(buyerId);
        logger.debug("Cart items found: " + (items != null ? items.size() : "null"));
        return items;
    }

    public boolean removeFromCart(int cartId) {
        logger.info("Removing cart item with cartId: " + cartId);
        boolean success = cartDAO.removeFromCart(cartId);
        if (success) {
            logger.info("Removed cart item successfully: cartId " + cartId);
        } else {
            logger.warn("Failed to remove cart item or not found: cartId " + cartId);
        }
        return success;
    }

    // ** NEW: Calculate total amount in cart **
    public double calculateCartTotal(int buyerId) {
        logger.info("Calculating cart total for buyerId: " + buyerId);
        List<CartItem> cartItems = cartDAO.getCartByBuyer(buyerId);
        double total = 0.0;

        for (CartItem ci : cartItems) {
            Product p = new ProductService().getProductById(ci.getProductId());
            if (p != null) {
                double sub = p.getPrice() * ci.getQuantity();
                logger.debug("CartItem: productId " + ci.getProductId() +
                             ", qty " + ci.getQuantity() + ", subTotal " + sub);
                total += sub;
            } else {
                logger.warn("Product not found for cartItem: " + ci.getCartId());
            }
        }

        logger.info("Cart total for buyerId " + buyerId + " = ₹" + total);
        return total;
    }
}
