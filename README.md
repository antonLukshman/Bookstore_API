# Bookstore API

A RESTful API for managing a bookstore system built with JAX-RS as part of the Client-Server Architectures coursework at the University of Westminster.

## 🚀 Features

- **CRUD Operations** for Books, Authors, Customers
- **Shopping Cart Management** with add/remove/update items
- **Order Processing** from cart to purchase completion
- **Custom Exception Handling** with meaningful error responses
- **In-Memory Data Storage** for demonstration purposes
- **RESTful Design** following best practices

## 🛠️ Technologies

- Java
- JAX-RS (Jersey)
- Apache Tomcat
- Maven
- JSON

## 📋 API Endpoints

### Books
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create new book
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Authors
- `GET /api/authors` - Get all authors
- `GET /api/authors/{id}` - Get author by ID
- `GET /api/authors/{id}/books` - Get books by author
- `POST /api/authors` - Create new author
- `PUT /api/authors/{id}` - Update author
- `DELETE /api/authors/{id}` - Delete author

### Customers
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Shopping Cart
- `GET /api/customers/{customerId}/cart` - Get customer's cart
- `POST /api/customers/{customerId}/cart/items` - Add item to cart
- `PUT /api/customers/{customerId}/cart/items/{bookId}` - Update cart item
- `DELETE /api/customers/{customerId}/cart/items/{bookId}` - Remove item from cart

### Orders
- `GET /api/customers/{customerId}/orders` - Get customer's orders
- `GET /api/customers/{customerId}/orders/{orderId}` - Get specific order
- `POST /api/customers/{customerId}/orders` - Create order from cart

## 🚦 Getting Started

1. Clone the repository
2. Open in NetBeans IDE
3. Build with Maven
4. Deploy to Apache Tomcat
5. Access API at `http://localhost:8080/BookstoreAPI/api`

## 🧪 Testing

Import the **Bookstore API.postman_collection.json** file into Postman to test all endpoints with both successful and error scenarios. The collection includes comprehensive test cases for every API endpoint.


---

*Developed by [Anton Luckshman]*
