package com.revshop.ui;

import java.util.List;
import java.util.Scanner;

import com.revshop.model.Product;
import com.revshop.model.Review;
import com.revshop.model.User;
import com.revshop.model.Notification;
import com.revshop.model.CartItem;
import com.revshop.model.Order;
import com.revshop.service.CartService;
import com.revshop.service.OrderService;
import com.revshop.service.ProductService;
import com.revshop.service.ReviewService;
import com.revshop.service.FavoriteService;
import com.revshop.service.NotificationService;
import com.revshop.service.UserService;

public class BuyerMenu {

    private Scanner sc = new Scanner(System.in);
    private ProductService productService = new ProductService();
    private CartService cartService = new CartService();
    private OrderService orderService = new OrderService();
    private ReviewService reviewService = new ReviewService();
    private FavoriteService favoriteService = new FavoriteService();
    private NotificationService notificationService = new NotificationService();
    private UserService userService=new UserService();

    private User buyer;

    public BuyerMenu(User buyer) {
        this.buyer = buyer;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Buyer Dashboard ===");
            System.out.println("1. Browse All Products");
            System.out.println("2. Browse by Category");
            System.out.println("3. Search Products");
            System.out.println("4. View Cart");
            System.out.println("5. Add Product to Cart");
            System.out.println("6. Remove from Cart");
            System.out.println("7. Checkout & Pay");
            System.out.println("8. View Order History");
            System.out.println("9. View Product Details (with Reviews)");
            System.out.println("10. Add Review & Rating");
            System.out.println("11. Save Product as Favorite");
            System.out.println("12. View Notifications");
            System.out.println("13. View favorites");
            System.out.println("14. Remove favorite");
            System.out.println("15. change Password");
            System.out.println("0. Logout");

            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: browseProducts(); break;
                case 2: browseByCategory(); break;
                case 3: searchProducts(); break;
                case 4: viewCart(); break;
                case 5: addToCartUI(); break;
                case 6: removeFromCartUI(); break;
                case 7: checkout(); break;
                case 8: viewOrderHistory(); break;
                case 9: viewProductDetails(); break;
                case 10: reviewProductUI(); break;
                case 11: saveFavoriteUI(); break;
                case 12: viewNotificationsUI(); break;
                case 13:viewFavoritesUI();break;
                case 14:removeFavoriteUI();break;
                case 15:changePasswordUI();break;

                case 0: System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private void browseProducts() {
        List<Product> products = productService.getAllProducts();
        System.out.println("\n--- All Products ---");
        for (Product p : products) {
            System.out.println(p.getProductId() + " | " + p.getName() + " | " +
                p.getCategory() + " | ₹" + p.getPrice());
        }
    }

    private void browseByCategory() {
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        List<Product> products = productService.getProductsByCategory(category);
        System.out.println("\n--- Products in Category: " + category + " ---");
        for (Product p : products) {
            System.out.println(p.getProductId() + " | " + p.getName() + " | ₹" + p.getPrice());
        }
    }

    private void searchProducts() {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine();
        List<Product> products = productService.searchProducts(keyword);
        System.out.println("\n--- Search Results ---");
        if (products.isEmpty()) System.out.println("No products found.");
        for (Product p : products) {
            System.out.println(p.getProductId() + " | " + p.getName() + " | ₹" + p.getPrice());
        }
    }

    private void viewCart() {
        List<CartItem> cartItems = cartService.viewCart(buyer.getUserId());
        System.out.println("\n--- Your Cart ---");
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }
        for (CartItem ci : cartItems) {
            System.out.println(ci.getCartId() + " | Product ID: " + ci.getProductId() +
                " | Qty: " + ci.getQuantity());
        }
    }

    private void addToCartUI() {
        System.out.print("Enter Product ID to add: ");
        int pid = Integer.parseInt(sc.nextLine());
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(sc.nextLine());

        boolean added = cartService.addToCart(buyer.getUserId(), pid, qty);
        System.out.println(added ? "✔ Added to cart!" : "✘ Failed to add to cart.");
    }

    private void removeFromCartUI() {
        System.out.print("Enter Cart ID to remove: ");
        int cartId = Integer.parseInt(sc.nextLine());
        boolean removed = cartService.removeFromCart(cartId);
        System.out.println(removed ? "✔ Removed from cart!" : "✘ Failed to remove from cart.");
    }

    private void checkout() {
        System.out.println("\n--- Checkout & Payment ---");

        List<CartItem> cartItems = cartService.viewCart(buyer.getUserId());
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        System.out.println("\nCart Summary:");
        double total = 0;
        for (CartItem ci : cartItems) {
            Product p = productService.getProductById(ci.getProductId());
            double sub = p.getPrice() * ci.getQuantity();
            total += sub;
            System.out.println(
                "Product: " + p.getName() +
                " | Qty: " + ci.getQuantity() +
                " | Price: ₹" + p.getPrice() +
                " | Subtotal: ₹" + sub
            );
        }
        System.out.println("---------------");
        System.out.println("Total: ₹" + total);

        System.out.print("\nShipping Address: ");
        String shipping = sc.nextLine();
        System.out.print("Billing Address: ");
        String billing = sc.nextLine();

        System.out.println("Select Payment Method:");
        System.out.println("1. Card");
        System.out.println("2. UPI");
        System.out.println("3. COD");
        System.out.print("Choice: ");
        int method = Integer.parseInt(sc.nextLine());
        String payMethod;

        switch (method) {
            case 1: payMethod = "Card"; break;
            case 2: payMethod = "UPI"; break;
            case 3: payMethod = "COD"; break;
            default:
                System.out.println("Invalid payment!");
                return;
        }

        System.out.println("Processing " + payMethod + " payment...");
        System.out.println("✔ Payment successful!");

        boolean placed = orderService.placeOrderTransactional(
            buyer.getUserId(), shipping, billing);

        if (placed) {
            System.out.println("✔ Order placed successfully!");
        } else {
            System.out.println("✘ Order failed!");
        }
    }

    private void viewOrderHistory() {
        List<Order> orders = orderService.getOrderHistory(buyer.getUserId());
        System.out.println("\n--- Order History ---");
        if (orders.isEmpty()) System.out.println("No past orders found!");
        for (Order o : orders) {
            System.out.println(o.getOrderId() + " | ₹" + o.getTotalAmount() +
                " | " + o.getStatus());
        }
    }

    private void viewProductDetails() {
        System.out.print("Enter Product ID to view details: ");
        int pid = Integer.parseInt(sc.nextLine());

        Product p = productService.getProductById(pid);
        if (p == null) {
            System.out.println("❌ Product not found!");
            return;
        }

        System.out.println("\n--- Product Details ---");
        System.out.println("ID: " + p.getProductId());
        System.out.println("Name: " + p.getName());
        System.out.println("Description: " + p.getDescription());
        System.out.println("Category: " + p.getCategory());
        System.out.println("Price: ₹" + p.getPrice());
        System.out.println("Discount Price: ₹" + p.getDiscountPrice());
        System.out.println("Stock: " + p.getStock());

        List<Review> reviews = reviewService.getReviewsForProduct(pid);
        System.out.println("\nReviews:");
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet.");
        } else {
            for (Review r : reviews) {
                System.out.println("⭐ " + r.getRating() + " : " + r.getReviewText());
            }
        }
    }

    private void reviewProductUI() {
        System.out.print("Enter Product ID to review: ");
        int pid = Integer.parseInt(sc.nextLine());
        System.out.print("Enter rating (1–5): ");
        int rating = Integer.parseInt(sc.nextLine());
        System.out.print("Enter your review: ");
        String text = sc.nextLine();

        boolean done = reviewService.addReview(buyer.getUserId(), pid, rating, text);
        System.out.println(done ? "✔ Review added!" : "✘ Review failed!");
    }

    private void saveFavoriteUI() {
        System.out.print("Enter Product ID to favorite: ");
        int pid = Integer.parseInt(sc.nextLine());

        boolean added = favoriteService.addFavorite(buyer.getUserId(), pid);
        System.out.println(added ? "✔ Product saved as favorite!" : "✘ Failed to save favorite!");
    }
    private void viewNotificationsUI() {
        List<Notification> list = notificationService.getNotifications(buyer.getUserId());
        notificationService.printNotifications(list);

        System.out.print("Enter notification ID to mark as read (0 to skip): ");
        int nid = Integer.parseInt(sc.nextLine());
        if (nid > 0) {
            notificationService.markNotificationAsRead(nid);
            System.out.println("Marked as read.");
        }
    }
    private void viewFavoritesUI() {
        java.util.List<com.revshop.model.Product> favs =
            favoriteService.getFavorites(buyer.getUserId());

        System.out.println("\n--- Your Favorites ---");
        if (favs.isEmpty()) {
            System.out.println("No favorites yet.");
            return;
        }
        for (com.revshop.model.Product p : favs) {
            System.out.println(p.getProductId() + " | " + p.getName() +
                " | ₹" + p.getPrice());
        }
    }

    private void removeFavoriteUI() {
        System.out.print("Enter Product ID to remove from favorites: ");
        int pid = Integer.parseInt(sc.nextLine());

        boolean done = favoriteService.removeFavorite(buyer.getUserId(), pid);
        System.out.println(done ? "✔ Removed from favorites!"
                               : "✘ Couldn’t remove favorite!");
    }
    private void changePasswordUI() {
        System.out.print("Enter old password: ");
        String oldPass = sc.nextLine();
        System.out.print("Enter new password: ");
        String newPass = sc.nextLine();

        boolean ok = userService.changePassword(buyer.getUserId(), oldPass, newPass);
        System.out.println(ok ? "✔ Password updated!" : "✘ Update failed!");
    }


}
