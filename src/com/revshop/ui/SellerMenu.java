package com.revshop.ui;

import java.util.List;
import java.util.Scanner;

import com.revshop.model.Order;
import com.revshop.model.Product;
import com.revshop.model.Review;
import com.revshop.model.User;
import com.revshop.model.Notification;
import com.revshop.service.NotificationService;
import com.revshop.service.OrderService;
import com.revshop.service.ProductService;
import com.revshop.service.ReviewService;
import com.revshop.service.UserService;

public class SellerMenu {

    private Scanner sc = new Scanner(System.in);
    private ProductService productService = new ProductService();
    private OrderService orderService = new OrderService();
    private ReviewService reviewService = new ReviewService();
    private NotificationService notificationService = new NotificationService();
    private UserService userService=new UserService();

    private User seller;

    public SellerMenu(User seller) {
        this.seller = seller;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Seller Dashboard ===");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View My Products");
            System.out.println("5. View Orders For My Products");
            System.out.println("6. View Reviews for My Products");
            System.out.println("7. View Notifications");
            System.out.println("8. Change Password");

            System.out.println("0. Logout");

            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: addProductUI(); break;
                case 2: updateProductUI(); break;
                case 3: deleteProductUI(); break;
                case 4: viewMyProductsUI(); break;
                case 5: viewOrdersForMyProductsUI(); break;
                case 6: viewReviewsForMyProductsUI(); break;
                case 7: viewSellerNotificationsUI(); break;
                case 8: changeSellerPasswordUI(); break;


                case 0: System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    private void viewSellerNotificationsUI() {
        List<Notification> list = notificationService.getNotifications(seller.getUserId());
        notificationService.printNotifications(list);

        System.out.print("Enter notification ID to mark as read (0 to skip): ");
        int nid = Integer.parseInt(sc.nextLine());
        if (nid > 0) {
            notificationService.markNotificationAsRead(nid);
            System.out.println("Marked as read.");
        }
    }

    private void addProductUI() {
        System.out.println("\n--- Add New Product ---");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();
        System.out.print("Category: ");
        String cat = sc.nextLine();
        System.out.print("Price: ₹");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Discount Price: ₹");
        double dprice = Double.parseDouble(sc.nextLine());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(sc.nextLine());
        System.out.print("Stock Threshold: ");
        int threshold = Integer.parseInt(sc.nextLine());

        Product p = new Product();
        p.setSellerId(seller.getUserId());
        p.setName(name);
        p.setDescription(desc);
        p.setCategory(cat);
        p.setPrice(price);
        p.setDiscountPrice(dprice);
        p.setStock(stock);
        p.setStockThreshold(threshold);

        boolean success = productService.addProduct(p);
        System.out.println(success ? "✔ Product added!" : "✘ Failed to add product!");
    }

    private void updateProductUI() {
        System.out.print("Enter Product ID to update: ");
        int pid = Integer.parseInt(sc.nextLine());

        Product p = productService.getProductById(pid);
        if (p == null || p.getSellerId() != seller.getUserId()) {
            System.out.println("❌ Product not found or not yours!");
            return;
        }

        System.out.println("Editing product – leave blank to keep existing value.");

        System.out.print("New Name (" + p.getName() + "): ");
        String name = sc.nextLine();
        if (!name.trim().isEmpty()) p.setName(name);

        System.out.print("New Description (" + p.getDescription() + "): ");
        String desc = sc.nextLine();
        if (!desc.trim().isEmpty()) p.setDescription(desc);

        System.out.print("New Category (" + p.getCategory() + "): ");
        String cat = sc.nextLine();
        if (!cat.trim().isEmpty()) p.setCategory(cat);

        System.out.print("New Price (" + p.getPrice() + "): ₹");
        String priceStr = sc.nextLine();
        if (!priceStr.trim().isEmpty()) p.setPrice(Double.parseDouble(priceStr));

        System.out.print("New Discount (" + p.getDiscountPrice() + "): ₹");
        String discStr = sc.nextLine();
        if (!discStr.trim().isEmpty()) p.setDiscountPrice(Double.parseDouble(discStr));

        System.out.print("New Stock (" + p.getStock() + "): ");
        String stockStr = sc.nextLine();
        if (!stockStr.trim().isEmpty()) p.setStock(Integer.parseInt(stockStr));

        System.out.print("New Threshold (" + p.getStockThreshold() + "): ");
        String threshStr = sc.nextLine();
        if (!threshStr.trim().isEmpty()) p.setStockThreshold(Integer.parseInt(threshStr));

        boolean updated = productService.updateProduct(p);

        if (updated) {
            System.out.println("✔ Product updated!");

            // Low inventory notification
            if (p.getStock() <= p.getStockThreshold()) {
                notificationService.addNotification(
                    seller.getUserId(),
                    "⚠️ Low stock for '" + p.getName() +
                    "'. Current stock: " + p.getStock()
                );
                System.out.println("⚠️ Low inventory notification created.");
            }

        } else {
            System.out.println("✘ Update failed!");
        }
    }


    private void deleteProductUI() {
        System.out.print("Enter Product ID to delete: ");
        int pid = Integer.parseInt(sc.nextLine());

        Product p = productService.getProductById(pid);
        if (p == null || p.getSellerId() != seller.getUserId()) {
            System.out.println("❌ Product not found or not yours!");
            return;
        }

        boolean removed = productService.deleteProduct(pid);
        System.out.println(removed ? "✔ Product deleted!" : "✘ Delete failed!");
    }

    private void viewMyProductsUI() {
        List<Product> list = productService.getProductsBySeller(seller.getUserId());
        System.out.println("\n--- My Products ---");
        if (list.isEmpty()) System.out.println("No products yet.");
        for (Product p : list) {
            System.out.println(p.getProductId() + " | " + p.getName() + " | ₹" + p.getPrice());
        }
    }

    private void viewOrdersForMyProductsUI() {
        List<Order> orders = orderService.getOrdersForSeller(seller.getUserId());
        System.out.println("\n--- Orders for My Products ---");
        if (orders.isEmpty()) System.out.println("No orders found!");
        for (Order o : orders) {
            System.out.println(o.getOrderId() + " | Buyer: " + o.getBuyerId() +
                " | Total: ₹" + o.getTotalAmount());
        }
    }

    private void viewReviewsForMyProductsUI() {
        System.out.print("Enter Product ID to view reviews: ");
        int pid = Integer.parseInt(sc.nextLine());

        List<Review> reviews = reviewService.getReviewsForProduct(pid);
        System.out.println("\n--- Reviews ---");
        if (reviews.isEmpty()) System.out.println("No reviews yet.");
        for (Review r : reviews) {
            System.out.println("⭐ " + r.getRating() + " | " + r.getReviewText());
        }
    }
    private void changeSellerPasswordUI() {
        System.out.print("Old password: ");
        String oldPass = sc.nextLine();
        System.out.print("New password: ");
        String newPass = sc.nextLine();

        boolean ok = userService.changePassword(seller.getUserId(), oldPass, newPass);
        System.out.println(ok ? "✔ Password updated!" : "✘ Update failed!");
    }

}
