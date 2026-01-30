# 📘 **RevShop Project**

## **Java Console-Based E-Commerce Application**

---

## 🧾 **Project Summary**

RevShop is a secure console-based e-commerce application built using **Java 1.7**, **Oracle 10g Database**, and follows a **layered architecture (UI → Service → DAO → Database).**

This project simulates the core functionality of an online marketplace where:

* **Buyers** can browse products, manage carts, place orders, review products, and receive notifications.
* **Sellers** can manage inventory, add/update/delete products, view orders, and get alerts when stock is low.

It is designed to be modular and maintainable, allowing future expansion into web or microservices.

---

## 🚀 **Features Implemented**

### 🛍 Buyer Features

1. User registration with email, password, security question, and hint
2. Login and logout
3. Browse all products
4. Search products by keyword and category
5. Add/remove products to/from cart
6. View cart
7. Checkout with shipping and billing information
8. Simulated payment methods (Card, UPI, COD)
9. View order history
10. Review and rate products
11. Save products as favorites
12. View and manage notifications
13. Forget password recovery
14. Change account password

---

### 🏪 Seller Features

1. Seller registration and login
2. Add new products (price, discount, category)
3. Update product details
4. Delete products
5. View own products
6. View orders containing seller’s products
8. View product reviews
9. Receive low-stock notifications
10. Manage persistent notifications
11. Change account password

---

## 📦 **Project Architecture**

RevShop is developed using a **layered architecture**:

```
UI Layer (Menus)
 ↓
Service Layer (Business Logic)
 ↓
DAO Layer (Database Interaction)
 ↓
Oracle 10g Database (Tables + Sequences)
```

### **Layer Responsibilities**

**UI Layer**

* Contains interactive menus and input/output with the user.
* Classes: `MainMenu`, `BuyerMenu`, `SellerMenu`.

**Service Layer**

* Handles business logic and coordinates between UI and DAOs.
* Classes: `UserService`, `ProductService`, `CartService`, `OrderService`, `ReviewService`, `FavoriteService`, `NotificationService`.

**DAO Layer**

* Performs database operations (INSERT, SELECT, UPDATE, DELETE).
* Example classes: `UserDAO`, `ProductDAO`, `CartDAO`, etc.

**Database**

* Oracle 10g tables and sequences enforce integrity and generate IDs.

---

## 🧠 **How Layers Interact**

Example flow:

```
User Input (UI) → Service Method → DAO SQL → Database
```

Models (like `User`, `Product`, `Order`, etc.) pass between layers with data.

---

## 🧬 **Entity Relationship Diagram (ERD)**

Here’s the ASCII ERD illustrating your database:

```
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
```

> You can include a proper image in your README by exporting this ERD in tools like dbdiagram.io and then adding with Markdown:
>
> `![RevShop ERD](images/RevShop_ERD.png)`

---

## 🛠 **Database Structure Summary**

| Table             | Purpose                       |
| ----------------- | ----------------------------- |
| `user_1`          | Stores buyers and sellers     |
| `product_1`       | Product catalog               |
| `cart_1`          | Shopping cart items           |
| `orders_1`        | Order headers                 |
| `order_item_1`    | Individual order lines        |
| `review_1`        | Customer product reviews      |
| `favorite_1`      | Saved favorites               |
| `notifications_1` | Persistent user notifications |

Each table gets primary key values from sequences like:

```
user_seq, product_seq, cart_seq, order_seq, order_item_seq, review_seq, notification_seq
```

---

## 🧩 **Input Validation & Logging**

Before database calls, inputs are validated:

✔ Valid email format
✔ Password requirements
✔ Check product existence before cart operations
✔ Ensure quantity ≤ stock

Logging is done using **Log4j**, with both console and file logging enabled.

Example configuration (log4j.properties):

```
log4j.rootLogger=DEBUG, console, file
log4j.appender.console=org.apache.log4j.ConsoleAppender
...
log4j.appender.file=org.apache.log4j.RollingFileAppender
...
```

This helps in debugging and keeping track of application behavior.

---

## 🧪 **Running the Project**

### Prerequisites:

✔ Java 1.7
✔ Eclipse Indigo
✔ Oracle 10g database
✔ JDBC driver included in project
✔ log4j.properties in `src/`

### Steps:

1. Create the database tables and sequences in Oracle
2. Configure Oracle XE connection parameters
3. Run `RevShop.main()` class
4. Interact using the console menu

---

## 🧠 **How to Use the Menus**

### Main Menu:

```
1. Buyer Register  
2. Buyer Login  
3. Seller Register  
4. Seller Login  
5. Forgot Password  
0. Exit
```

After buyer login, available options include:

```
Browse products → 1  
Add to cart → 5  
View cart → 4  
Checkout → 7  
View favorites → 13  
View notifications → 12  
Change password → 15
```

---

## 📌 **Future Enhancements**

These features were considered but are not implemented in this version:

✔ Product sorting by price/rating
✔ More advanced filters (price range, category facets)
✔ Admin panel for system oversight
✔ Web or mobile front-end integration
✔ Real payment gateway integration
✔ Comprehensive unit & integration tests for all modules

---

## 📄 **Project Details / Credits**

* **Developed by**: Prudhvi Teja Garapati – 2354
* **Language**: Java 1.7
* **Database**: Oracle 10g
* **IDE**: Eclipse Indigo

---
