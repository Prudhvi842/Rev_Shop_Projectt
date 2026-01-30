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

    // --- Validation Helpers ---

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPassword(String pass) {
        if (pass == null || pass.trim().isEmpty()) return false;
        if (pass.length() < 6) return false;
        return pass.matches("^(?=.*[A-Za-z])(?=.*\\d).+$");
    }

    // --- Registration & Login ---

    private void buyerRegister() {
        System.out.println("\n=== Buyer Registration ===");

        System.out.print("Name: ");
        String name = sc.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Email: ");
        String email = sc.nextLine();
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (!isValidPassword(pass)) {
            System.out.println("Password must be at least 6 chars, with letters and numbers!");
            return;
        }

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
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (!isValidPassword(pass)) {
            System.out.println("Invalid password format!");
            return;
        }

        User user = userService.loginUser(email, pass);
        if (user != null && "BUYER".equals(user.getRole())) {
            System.out.println("✔ Login successful! Welcome " + user.getName());
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
        if (name.trim().isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Email: ");
        String email = sc.nextLine();
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (!isValidPassword(pass)) {
            System.out.println("Password must be at least 6 chars, with letters and numbers!");
            return;
        }

        System.out.print("Security Question (e.g., What is your pet’s name?): ");
        String secQ = sc.nextLine();
        System.out.print("Security Answer: ");
        String secA = sc.nextLine();
        System.out.print("Password Hint: ");
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
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (!isValidPassword(pass)) {
            System.out.println("Invalid password format!");
            return;
        }

        User user = userService.loginUser(email, pass);
        if (user != null && "SELLER".equals(user.getRole())) {
            System.out.println("✔ Login successful! Welcome " + user.getName());
            showNotificationsOnLogin(user.getUserId());
            new SellerMenu(user).show();
        } else {
            System.out.println("✘ Invalid credentials or not a seller!");
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

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

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
        if (!isValidPassword(newPass)) {
            System.out.println("New password must be at least 6 chars, with letters and numbers!");
            return;
        }

        boolean ok = userService.recoverPassword(email, answer, newPass);
        System.out.println(ok ? "✔ Password has been reset!" : "✘ Answer didn’t match!");
    }

}
