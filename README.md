Rev_Shop_Project

🧾 Project Summary

RevShop is a secure, console-based e-commerce application built with Java 1.7 and Oracle 10g.
It supports both buyers and sellers, allowing:

✔ Buyers to browse products, manage carts, place orders, review purchases, favorites, and receive notifications.
✔ Sellers to manage inventory, products, orders, and receive alerts for low stock.

The architecture is layered and modular — making it maintainable and clearly separated between UI, business logic, data access, and persistence.

🚀 Features Implemented
🛍 Buyer Features

✔ Register with email, password, security question/answer
✔ Login and logout
✔ Browse all products
✔ Search by keyword and filter by category
✔ Add / remove products to cart
✔ View cart contents
✔ Checkout with shipping/billing
✔ Simulated payment methods (Card, UPI, COD)
✔ View order history
✔ Review and rate products
✔ Save products as favorites
✔ View and manage notifications
✔ Forget password recovery
✔ Change account password

🏪 Seller Features

✔ Register and Login
✔ Add new products (price, discount, category)
✔ Update existing products
✔ Delete products
✔ View seller’s own products
✔ View orders containing seller’s products
✔ View reviews for products
✔ Receive low stock notifications
✔ Manage persistent notifications
✔ Change account password

📦 Architecture Overview

RevShop follows a standard layered architecture:

UI Layer (Menus)
    ↓
Service Layer (business logic)
    ↓
DAO Layer (data access)
    ↓
Oracle 10g Database

Layer Responsibilities

UI Layer

Classes: MainMenu, BuyerMenu, SellerMenu

Handles input/output and basic validation

Service Layer

Coordinators of business rules

Classes: UserService, ProductService, CartService, OrderService, ReviewService, FavoriteService, NotificationService

DAO Layer

Performs SQL queries and updates

Classes: UserDAO, ProductDAO, CartDAO, OrderDAO, etc.

Database

Oracle 10g tables + sequences

Referential constraints enforce integrity

🧠 How the Layers Interact
User Input (UI) → Service Method → DAO SQL → DB Storage


Models (like User, Product, Order, etc.) are passed between layers.

🧬 Entity Relationship Diagram (ERD)

Here’s an ASCII version you can include directly in your README:

                               +----------------+
                               |    user_1      |
                               +----------------+
                               | PK user_id     |
                               | name           |
                               | email          |
                               | password       |
                               | role           |
                               | security_q     |
                               | security_a     |
                               | password_hint  |
                               +----------------+
                                       |
                 ---------------------------------------------
                 |                       |                     |
                 v                       v                     v
         +---------------+       +---------------+     +----------------+
         |  product_1    |       |   cart_1      |     | notifications_1|
         +---------------+       +---------------+     +----------------+
         | PK product_id |       | PK cart_id    |     | PK notification_id |
         | seller_id (FK)|◄──────┤ buyer_id (FK) |     | user_id (FK)       |
         | name          |       | product_id(FK)|     | message            |
         | description   |       | quantity      |     | is_read            |
         | category      |       +---------------+     | created_date       |
         | price         |                             +--------------------+
         | discountPrice |
         | stock         |
         | stockThreshold|
         +---------------+
                 |
                 |
                 v
         +----------------+
         |   review_1     |
         +----------------+
         | PK review_id   |
         | buyer_id (FK)  |
         | product_id(FK) |
         | rating         |
         | comment        |
         | review_date    |
         +----------------+

                 |
                 v
         +----------------+
         | favorite_1     |
         +----------------+
         | buyer_id (FK)  |
         | product_id(FK) |
         +----------------+

                 |
                 v
         +----------------+
         |   orders_1     |
         +----------------+
         | PK order_id    |
         | buyer_id (FK)  |
         | total_amount   |
         | order_date     |
         | status         |
         | shipping_addr  |
         | billing_addr   |
         +----------------+
                 |
                 v
         +----------------+
         | order_item_1   |
         +----------------+
         | PK order_item_id |
         | order_id (FK)    |
         | product_id(FK)   |
         | quantity         |
         | price_each       |
         +----------------+


Tip: You can include a proper image in your README using Markdown if you export the ERD from a tool.

Example:

![RevShop ERD](docs/RevShop_ERD.png)

🛠 Database Structure Summary
Table	Purpose
user_1	Stores users (buyers & sellers)
product_1	Product catalog
cart_1	Items added to cart
orders_1	Order header records
order_item_1	Order_line items
review_1	Buyer reviews
favorite_1	Saved favorites
notifications_1	Persistent alerts

Each table uses sequences such as:

user_seq, product_seq, cart_seq, order_seq, order_item_seq, review_seq, notification_seq

🧩 Input Validation & Logging
Validation

Before database calls, inputs are validated:

✔ Valid email format
✔ Valid password strength
✔ Existing product IDs
✔ Quantity ≤ current stock

This prevents database errors and improves user experience.

Logging (Log4j + Commons Logging)

You included this log4j.properties to capture logs:

# Root logger
log4j.rootLogger=DEBUG, console, file

# Console appender
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.Target=System.out
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%d{ISO8601} [%p] %c: %m%n

# File appender
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=logs/revshop.log
log4j.appender.file.MaxFileSize=5MB
log4j.appender.file.MaxBackupIndex=3
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ISO8601} [%p] %c: %m%n


Console logging helps during development

File logging (logs/revshop.log) gives audit trail

🧪 Running the Project
Prerequisites

✔ Java 1.7
✔ Eclipse Indigo
✔ Oracle 10g database
✔ JDBC driver in project classpath

Steps

Create tables in Oracle

Create sequences for ID generation

Place log4j.properties in src/

Run RevShop.main() class

Interact via console menus

🧠 How to Use Menus
Main Menu
1. Buyer Register
2. Buyer Login
3. Seller Register
4. Seller Login
5. Forgot Password
0. Exit

Buyer Dashboard Exercise

After login:

Browse products → 1
Add to cart → 5
View cart → 4
Checkout → 7
View favorites → 13
View notifications → 12
Change password → 15

📌 Future Enhancements

✔ Product sorting (by price, rating)
✔ Filter by price range
✔ Admin panel for global management
✔ Web/Mobile front-end integration
✔ Unit tests (JUnit) for more classes

📄 Contact / Credits

Developed by: Prudhvi Teja Garapati-2354
Language: Java 1.7
Database: Oracle 10g
IDE: Eclipse Indigo
