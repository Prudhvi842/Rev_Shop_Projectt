package com.revshop.ui;

import java.util.Scanner;

import com.revshop.service.NotificationService;
import com.revshop.service.UserService;
import com.revshop.model.User;

public class MainMenu {

    private Scanner sc = new Scanner(System.in);
    private UserService userService = new UserService();

    public void show() {
        while (true) {
            System.out.println("\n=== RevShop Main Menu ===");
            System.out.println("1. Buyer Register");
            System.out.println("2. Buyer Login");
            System.out.println("3. Seller Register");
            System.out.println("4. Seller Login");
            System.out.println("5. Forgot password");
            System.out.println("0. Exit");

            System.out.print("Select option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: buyerRegister(); break;
                case 2: buyerLogin(); break;
                case 3: sellerRegister(); break;
                case 4: sellerLogin(); break;
                case 5: forgotPasswordUI(); break;

                case 0: System.out.println("Thank you for using RevShop!"); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private void buyerRegister() {
        System.out.println("\n=== Buyer Registration ===");

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Security Question (e.g., Your favorite teacher?): ");
        String secQ = sc.nextLine();

        System.out.print("Security Answer: ");
        String secA = sc.nextLine();

        System.out.print("Password Hint: ");
        String hint = sc.nextLine();

        boolean success = userService.registerUserWithSecurity(
            name, email, pass, "BUYER",
            secQ, secA, hint
        );

        System.out.println(success
            ? "✔ Buyer registered successfully!"
            : "✘ Buyer registration failed!");
    }


    private void buyerLogin() {
        System.out.println("\n=== Buyer Login ===");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User user = userService.loginUser(email, pass);
        if (user != null && "BUYER".equals(user.getRole())) {
            System.out.println("✔ Login successful! Welcome " + user.getName());

            // Show unread notifications
            showNotificationsOnLogin(user.getUserId());

            new BuyerMenu(user).show();
        } else {
            System.out.println("✘ Invalid credentials or not a buyer!");
        }

    }

    private void sellerRegister() {
        System.out.println("\n=== Seller Registration ===");

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        // --- New fields for password recovery ---
        System.out.print("Security Question (e.g., What is your pet’s name?): ");
        String secQ = sc.nextLine();

        System.out.print("Security Answer: ");
        String secA = sc.nextLine();

        System.out.print("Password Hint (short hint to help you remember): ");
        String hint = sc.nextLine();

        boolean success = userService.registerUserWithSecurity(
            name, email, pass, "SELLER",
            secQ, secA, hint
        );

        System.out.println(success
            ? "✔ Seller registered successfully!"
            : "✘ Seller registration failed!");
    }


    private void sellerLogin() {
        System.out.println("\n=== Seller Login ===");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User user = userService.loginUser(email, pass);
        if (user != null && "SELLER".equals(user.getRole())) {
            System.out.println("✔ Login successful! Welcome " + user.getName());

            showNotificationsOnLogin(user.getUserId());

            new SellerMenu(user).show();
        }
        else{
        	System.out.println("Invalid credentials or not a seller");
        }

    }
    private void showNotificationsOnLogin(int userId) {
        NotificationService notificationService = new NotificationService();
        java.util.List<com.revshop.model.Notification> notis =
            notificationService.getNotifications(userId);

        System.out.println("\n=== You have notifications ===");
        boolean hasNew = false;
        for (com.revshop.model.Notification n : notis) {
            if ("N".equalsIgnoreCase(n.getIsRead())) {
                System.out.println("🔔 " + n.getMessage());
                notificationService.markNotificationAsRead(n.getNotificationId());
                hasNew = true;
            }
        }
        if (!hasNew) System.out.println("No new notifications.");
        System.out.println();
    }
    private void forgotPasswordUI() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Password Recovery ---");
        System.out.print("Enter your registered email: ");
        String email = sc.nextLine();

        // Fetch password hint
        String hint = userService.getPasswordHint(email);
        if (hint == null) {
            System.out.println("Email not found!");
            return;
        }
        System.out.println("Password Hint: " + hint);

        System.out.print("Answer Security Question: ");
        User user = userService.getUserByEmail(email);

        if (user.getSecurityQuestion() != null && !user.getSecurityQuestion().trim().isEmpty()) {
            System.out.println(user.getSecurityQuestion());
        }

        System.out.print("Enter your answer: ");
        String answer = sc.nextLine();

        System.out.print("Enter new password: ");
        String newPass = sc.nextLine();

        boolean ok = userService.recoverPassword(email, answer, newPass);
        System.out.println(ok ? "✔ Password has been reset!" : "✘ Answer didn’t match!");
    }

}
