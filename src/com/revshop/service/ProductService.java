package com.revshop.service;

import com.revshop.dao.ProductDAO;
import com.revshop.model.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class ProductService {

    private static final Log logger = LogFactory.getLog(ProductService.class);

    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> list = productDAO.getAllProducts();
        logger.debug("Total products found: " + (list != null ? list.size() : "null"));
        return list;
    }

    public List<Product> searchProducts(String keyword) {
        logger.info("Searching products with keyword: " + keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            logger.warn("Empty search keyword provided");
            return productDAO.getAllProducts();
        }
        List<Product> results = productDAO.searchProducts(keyword);
        logger.debug("Products found for keyword '" + keyword + "': " + (results != null ? results.size() : "null"));
        return results;
    }

    public Product getProductById(int id) {
        logger.info("Fetching product by ID: " + id);
        Product p = productDAO.getProductById(id);
        if (p == null) {
            logger.warn("Product not found for ID: " + id);
        } else {
            logger.debug("Product found: " + p.getName());
        }
        return p;
    }

    public List<Product> getProductsByCategory(String category) {
        logger.info("Fetching products by category: '" + category + "'");
        if (category == null || category.trim().isEmpty()) {
            logger.warn("Empty category provided, returning all products");
            return getAllProducts();
        }
        List<Product> list = productDAO.getProductsByCategory(category.trim());
        logger.debug("Products found in category '" + category + "': " + (list != null ? list.size() : "null"));
        return list;
    }

    public boolean deleteProduct(int productId) {
        logger.info("Deleting product with ID: " + productId);
        boolean success = productDAO.deleteProduct(productId);
        if (success) {
            logger.info("Product deleted successfully: " + productId);
        } else {
            logger.warn("Failed to delete product: " + productId);
        }
        return success;
    }

    public List<Product> getProductsBySeller(int sellerId) {
        logger.info("Fetching products for sellerId: " + sellerId);
        List<Product> list = productDAO.getProductsBySeller(sellerId);
        logger.debug("Products found for sellerId " + sellerId + ": " + (list != null ? list.size() : "null"));
        return list;
    }

    public boolean updateProduct(Product product) {
        logger.info("Updating product: ID=" + (product != null ? product.getProductId() : "null"));
        if (product == null) {
            logger.error("Cannot update: product is null");
            return false;
        }
        if (product.getProductId() <= 0) {
            logger.warn("Invalid product ID for update: " + product.getProductId());
            return false;
        }

        boolean success = productDAO.updateProduct(product);
        if (success) {
            logger.info("Product updated successfully: ID=" + product.getProductId());
        } else {
            logger.error("Failed to update product: ID=" + product.getProductId());
        }
        return success;
    }

    public boolean addProduct(Product product) {
        logger.info("Adding new product: " + (product != null ? product.getName() : "null"));
        if (product == null) {
            logger.error("Cannot add product: product is null");
            return false;
        }
        int id = productDAO.addProduct(product);
        boolean success = id > 0;
        if (success) {
            logger.info("Product added successfully with ID: " + id);
        } else {
            logger.error("Failed to add product: " + product.getName());
        }
        return success;
    }

    public void checkAndNotifyLowInventory(Product p) {
        if (p == null) {
            logger.warn("Cannot check inventory: product is null");
            return;
        }
        logger.debug("Checking low inventory for product ID=" + p.getProductId());
        if (p.getStock() <= p.getStockThreshold()) {
            String msg = "⚠️ Low stock for '" + p.getName() + "'. Current stock: " + p.getStock();
            logger.warn(msg);
            new NotificationService().addNotification(p.getSellerId(), msg);
        }
    }
}
