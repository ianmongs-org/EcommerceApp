# E-Commerce Application

## Introduction

Welcome to the **E-Commerce Application**, a robust online shopping platform built using **Spring Boot**. This project provides an end-to-end solution for managing products, customers, orders, payments, and shopping carts. It is designed to be scalable, modular, and customizable, integrating a wide range of typical e-commerce features.

### Key Features
- **User Management**: Registration, login, role-based access, and profile management.
- **Product Management**: Add, update, delete, and categorize products.
- **Order Management**: Place orders, track order status, view order history.
- **Shopping Cart**: Add/remove items, view cart summary, update quantities.
- **Payment Integration**: Secure payment processing with integrated gateways.
- **Search and Filters**: Search for products by name, category, and price range.
- **Admin Panel**: A separate interface for admins to manage products, users, and orders.

## Project Structure

```
EcommerceApp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/ecommerce/
│   │   │   │   ├── controller/      # REST Controllers
│   │   │   │   ├── model/           # JPA Entity Classes
│   │   │   │   ├── repository/      # Data Repositories
│   │   │   │   ├── service/         # Business Logic Layer
│   │   │   │   └── security/        # Security Configurations
│   │   └── resources/
│   │       ├── application.properties  # Configuration properties
│   └── test/
│       └── java/                      # Unit and Integration Tests
├── pom.xml                             # Maven Dependencies
└── README.md                           # Project Documentation
```

## Technologies Used

- **Backend Framework**: Spring Boot (Java)
- **Database**: MySQL/PostgreSQL (JPA/Hibernate for ORM)
- **Security**: Spring Security with JWT authentication
- **Payment Integration**: Stripe/PayPal API
- **Frontend**: React/Angular (optional, not included in this repository)
- **Build Tool**: Maven
- **Testing**: JUnit, Mockito
- **Containerization**: Docker (optional)

## Requirements

- Java 11 or higher
- Maven 3.6+
- MySQL/PostgreSQL (or any supported RDBMS)
- IDE: IntelliJ IDEA / Eclipse / VSCode (with Java support)
- Docker (optional, for containerization)

## Installation

1. **Clone the repository**:
    ```
    git clone https://https://github.com/Dancan254/EcommerceApp.git
    cd EcommerceApp
    ```

2. **Configure the database**:
   Update the `application.properties` file with your database configuration:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    ```

3. **Build the project**:
    ```
    mvn clean install
    ```

4. **Run the application**:
    ```
    mvn spring-boot:run
    ```

5. **Access the API**:
   The application will start running on `http://localhost:8080`.

## Key Functionalities

### 1. **User Management**
   - **Registration**: Users can register using a sign-up endpoint. Passwords are encrypted using **BCrypt**.
   - **Authentication**: Login is managed using JWT (JSON Web Token) for stateless authentication.
   - **Role-Based Access Control**: Two roles are provided: `USER` and `ADMIN`. Admins have additional privileges for managing products and orders.

### 2. **Product Management**
   - **Admin CRUD Operations**: Admin users can add, update, or delete products via secured endpoints.
   - **Product Categories**: Products can be categorized for easy filtering.
   - **Search and Filters**: Products can be searched by name, category, and price range.

### 3. **Order Management**
   - **Placing Orders**: Users can place orders by adding products to their cart and proceeding to checkout.
   - **Order Tracking**: Users can view the status of their orders and track their shipment.
   - **Order History**: View past orders and re-order items.

### 4. **Shopping Cart**
   - **Add to Cart**: Users can add products to their cart.
   - **Update Cart**: Users can update item quantities or remove products.
   - **View Cart**: A detailed summary of the cart including product names, prices, and total amount.

### 5. **Payment Integration**
   - **Payment Gateway**: Payments are securely processed through Stripe or PayPal integration.
   - **Order Confirmation**: Upon successful payment, an order confirmation email is sent to the user.
   
### 6. **Security**
   - **JWT Authentication**: Secure stateless authentication for users.
   - **Password Encryption**: User passwords are hashed and stored securely.
   - **Role-Based Access**: Restricted access to certain features for admins and users.

## REST API Endpoints

### User API
| HTTP Method | Endpoint               | Description               |
|-------------|------------------------|---------------------------|
| POST        | `/api/auth/register`    | Register a new user       |
| POST        | `/api/auth/login`       | Login and obtain JWT      |
| GET         | `/api/users/{id}`       | Get user by ID            |

### Product API
| HTTP Method | Endpoint               | Description               |
|-------------|------------------------|---------------------------|
| GET         | `/api/products`         | Get all products          |
| GET         | `/api/products/{id}`    | Get product by ID         |
| POST        | `/api/products`         | Add new product (Admin)   |
| PUT         | `/api/products/{id}`    | Update product (Admin)    |
| DELETE      | `/api/products/{id}`    | Delete product (Admin)    |

### Order API
| HTTP Method | Endpoint               | Description               |
|-------------|------------------------|---------------------------|
| POST        | `/api/orders`           | Place a new order         |
| GET         | `/api/orders`           | Get all orders (Admin)    |
| GET         | `/api/orders/{id}`      | Get order by ID           |

### Cart API
| HTTP Method | Endpoint               | Description               |
|-------------|------------------------|---------------------------|
| POST        | `/api/carts`            | Add item to cart          |
| GET         | `/api/carts`            | Get user's cart           |
| DELETE      | `/api/carts/{itemId}`   | Remove item from cart     |

## Testing

- **Unit Tests**: For testing individual service methods.
- **Integration Tests**: To test the interaction between layers (e.g., service and repository).
  
To run tests:
```
mvn test
```

## Deployment

You can deploy the Spring Boot application in several ways:

1. **Local Deployment**: The app can be run locally by running the main method in the `EcommerceApplication.java` class.
   
2. **Docker Deployment**: Build and run the application inside a Docker container.
   - Build Docker Image:
     ```bash
     docker build -t ecommerce-app .
     ```
   - Run Docker Container:
     ```bash
     docker run -p 8080:8080 ecommerce-app
     ```

3. **Cloud Deployment**: You can deploy the app on cloud platforms like **AWS**, **Azure**, or **Heroku** by configuring the necessary settings.

## License

This project is licensed under the MIT License. Feel free to use and modify the code as per your requirements.

---
