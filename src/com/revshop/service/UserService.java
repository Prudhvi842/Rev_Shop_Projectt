package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserService {

    private static final Log logger = LogFactory.getLog(UserService.class);
    private UserDAO userDAO = new UserDAO();

    public boolean registerUser(String name, String email, String password, String role) {
        logger.info("Attempting user registration: email=" + email + ", role=" + role);

        if (name == null || email == null || password == null || role == null) {
            logger.warn("Registration failed: missing required fields");
            System.out.println("All fields are required for registration!");
            return false;
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(password.trim());
        user.setRole(role.trim().toUpperCase());

        boolean success = userDAO.addUser(user);
        if (success) {
            logger.info("User registered successfully: email=" + email);
        } else {
            logger.error("User registration failed for email=" + email);
        }
        return success;
    }

    public User loginUser(String email, String password) {
        logger.info("Login attempt: email=" + email);

        if (email == null || password == null) {
            logger.warn("Login failed: email or password is null");
            System.out.println("Email and Password are required!");
            return null;
        }

        User user = userDAO.getUserByEmailAndPassword(email.trim(), password.trim());
        if (user != null) {
            logger.info("Login successful: userId=" + user.getUserId());
        } else {
            logger.warn("Login failed: invalid credentials for email=" + email);
        }
        return user;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        logger.info("Change password requested for userId=" + userId);
        User u = userDAO.getUserById(userId);

        if (u == null) {
            logger.warn("Change password failed: user not found userId=" + userId);
            System.out.println("User not found!");
            return false;
        }
        if (!u.getPassword().equals(oldPassword)) {
            logger.warn("Change password failed: old password mismatch for userId=" + userId);
            System.out.println("Old password does not match!");
            return false;
        }

        boolean success = userDAO.updatePassword(userId, newPassword);
        if (success) {
            logger.info("Password changed successfully for userId=" + userId);
        } else {
            logger.error("Password change failed for userId=" + userId);
        }
        return success;
    }

    public boolean registerUserWithSecurity(
            String name, String email, String password, String role,
            String secQ, String secA, String hint
    ) {
        logger.info("Attempting user registration with security fields: email=" + email);

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(password.trim());
        user.setRole(role.trim().toUpperCase());
        user.setSecurityQuestion(secQ);
        user.setSecurityAnswer(secA);
        user.setPasswordHint(hint);

        boolean success = userDAO.addUser(user);
        if (success) {
            logger.info("User registered with security successfully: email=" + email);
        } else {
            logger.error("User registration with security failed: email=" + email);
        }
        return success;
    }

    public String getPasswordHint(String email) {
        logger.info("Fetching password hint for email=" + email);
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            logger.warn("No user found for password hint: email=" + email);
            return null;
        }
        logger.debug("Password hint retrieved for email=" + email);
        return user.getPasswordHint();
    }

    public boolean recoverPassword(String email, String answer, String newPassword) {
        logger.info("Password recovery attempt: email=" + email);
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            logger.warn("Password recovery failed: no user found for email=" + email);
            return false;
        }

        if (user.getSecurityAnswer() != null &&
            user.getSecurityAnswer().equalsIgnoreCase(answer.trim())) {
            boolean success = userDAO.updatePassword(user.getUserId(), newPassword);
            if (success) {
                logger.info("Password recovered successfully for userId=" + user.getUserId());
            } else {
                logger.error("Password recovery update failed for userId=" + user.getUserId());
            }
            return success;
        }

        logger.warn("Password recovery failed: incorrect security answer for email=" + email);
        return false;
    }

    // Method to get user details by email
    public User getUserByEmail(String email) {
        logger.info("Fetching user by email=" + email);
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            logger.debug("User found: userId=" + user.getUserId());
        } else {
            logger.warn("No user found for email=" + email);
        }
        return user;
    }

}
