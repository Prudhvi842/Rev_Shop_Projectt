package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.dao.OrderDAO;
import com.revshop.model.Order;
import com.revshop.model.CartItem;
import com.revshop.model.Product;
import com.revshop.dbutil.DB_Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class OrderService {

    private static final Log logger = LogFactory.getLog(OrderService.class);

    private OrderDAO orderDAO = new OrderDAO();
    private NotificationService notificationService = new NotificationService();

    public boolean placeOrderTransactional(int buyerId, String shipping, String billing) {
        logger.info("Starting transactional order placement for buyerId=" + buyerId);

        Connection conn = null;
        boolean success = false;

        try {
            conn = DB_Connection.getConnection();
            conn.setAutoCommit(false);

            // Retrieve cart
            List<CartItem> cartItems = orderDAO.getCartByBuyer(buyerId, conn);
            if (cartItems == null || cartItems.isEmpty()) {
                logger.warn("Cart is empty for buyerId=" + buyerId + ", aborting order.");
                return false;
            }
            logger.debug("Cart items count: " + cartItems.size());

            // Calculate total
            double total = 0;
            for (CartItem ci : cartItems) {
                Product p = new ProductService().getProductById(ci.getProductId());
                if (p == null) {
                    logger.error("Product not found in DB for productId=" + ci.getProductId());
                    conn.rollback();
                    return false;
                }
                double subTotal = p.getPrice() * ci.getQuantity();
                total += subTotal;
                logger.debug("Item productId=" + ci.getProductId() +
                             ", qty=" + ci.getQuantity() +
                             ", subTotal=₹" + subTotal);
            }
            logger.info("Total order amount calculated: ₹" + total);

            // Save order
            Order order = new Order();
            order.setBuyerId(buyerId);
            order.setOrderDate(new Date());
            order.setTotalAmount(total);
            order.setStatus("PLACED");
            order.setShippingAddress(shipping);
            order.setBillingAddress(billing);

            int orderId = orderDAO.saveOrderReturnId(order, conn);
            logger.info("Order record created with orderId=" + orderId);

            // Save order items and adjust stock
            for (CartItem ci : cartItems) {
                Product p = new ProductService().getProductById(ci.getProductId());

                orderDAO.saveOrderItem(orderId, ci.getProductId(), ci.getQuantity(), p.getPrice(), conn);
                logger.debug("Order item saved --> orderId=" + orderId + ", productId=" + ci.getProductId());

                int newStock = p.getStock() - ci.getQuantity();
                orderDAO.updateProductStock(p.getProductId(), newStock, conn);
                logger.info("Stock updated for productId=" + ci.getProductId() + " newStock=" + newStock);

                if (newStock <= p.getStockThreshold()) {
                    notificationService.addNotification(
                        p.getSellerId(),
                        "⚠️ Low stock for '" + p.getName() + "'. Current stock: " + newStock
                    );
                    logger.warn("Low stock notification created for sellerId=" + p.getSellerId() +
                                " productId=" + p.getProductId());
                }
            }

            // Clear cart
            new CartDAO().clearCartByBuyer(buyerId, conn);
            logger.info("Cleared cart for buyerId=" + buyerId);

            // Commit transaction
            conn.commit();
            success = true;
            logger.info("Transaction committed successfully for orderId=" + orderId);

            // Send notifications
            notificationService.addNotification(
                buyerId,
                "Your order #" + orderId + " of ₹" + total + " has been placed successfully!"
            );
            logger.info("Order confirmation notification sent to buyerId=" + buyerId);

            List<Integer> sellers = orderDAO.getSellersForOrder(orderId);
            if (sellers != null) {
                for (Integer sellerId : sellers) {
                    notificationService.addNotification(
                        sellerId,
                        "📦 New order (#" + orderId + ") placed!"
                    );
                    logger.info("New order notification sent to sellerId=" + sellerId);
                }
            }

        } catch (Exception e) {
            logger.error("Error during transactional order placement for buyerId=" + buyerId, e);
            try {
                if (conn != null) {
                    conn.rollback();
                    logger.warn("Transaction rolled back for buyerId=" + buyerId);
                }
            } catch (Exception rollbackEx) {
                logger.error("Rollback failed for buyerId=" + buyerId, rollbackEx);
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex) {
                logger.error("Error closing connection for buyerId=" + buyerId, ex);
            }
        }

        return success;
    }

    public List<Order> getOrderHistory(int buyerId) {
        logger.info("Fetching order history for buyerId=" + buyerId);
        return orderDAO.getOrdersByBuyer(buyerId);
    }

    public java.util.List<Order> getOrdersForSeller(int sellerId) {
        logger.info("Fetching orders for sellerId=" + sellerId);
        return orderDAO.getOrdersForSeller(sellerId);
    }
}
