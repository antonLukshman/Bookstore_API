/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.storage;

import com.bookstore.api.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author USER
 */
/**
 * Singleton class for in-memory data storage
 */
public class DataStorage {
    private static DataStorage instance;
    
    // ID generators
    private final AtomicLong bookIdGenerator = new AtomicLong(1);
    private final AtomicLong authorIdGenerator = new AtomicLong(1);
    private final AtomicLong customerIdGenerator = new AtomicLong(1);
    private final AtomicLong orderIdGenerator = new AtomicLong(1);
    
    // Storage collections
    private final Map<Long, Book> books = new HashMap<>();
    private final Map<Long, Author> authors = new HashMap<>();
    private final Map<Long, Customer> customers = new HashMap<>();
    private final Map<Long, Cart> carts = new HashMap<>();
    private final Map<Long, List<Order>> customerOrders = new HashMap<>();
    
    // Private constructor to prevent instantiation
    private DataStorage() {
        // Initialize with some sample data
        initializeSampleData();
    }
    
    // Singleton getInstance method
    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }
    
    // Initialize with sample data
    private void initializeSampleData() {
        // Create authors
        Author author1 = new Author(authorIdGenerator.getAndIncrement(), "J.K. Rowling", "British author best known for the Harry Potter series.");
        Author author2 = new Author(authorIdGenerator.getAndIncrement(), "George Orwell", "English novelist, essayist, and critic.");
        
        authors.put(author1.getId(), author1);
        authors.put(author2.getId(), author2);
        
        // Create books
        Book book1 = new Book(bookIdGenerator.getAndIncrement(), "Harry Potter and the Philosopher's Stone", author1.getId(), "978-0-7475-3269-9", 1997, 9.99, 100);
        Book book2 = new Book(bookIdGenerator.getAndIncrement(), "1984", author2.getId(), "978-0-452-28423-4", 1949, 7.99, 50);
        
        books.put(book1.getId(), book1);
        books.put(book2.getId(), book2);
        
        // Create customers
        Customer customer1 = new Customer(customerIdGenerator.getAndIncrement(), "John Doe", "john@example.com", "password123");
        Customer customer2 = new Customer(customerIdGenerator.getAndIncrement(), "Jane Smith", "jane@example.com", "password456");
        
        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        
        // Create carts for customers
        carts.put(customer1.getId(), new Cart(customer1.getId()));
        carts.put(customer2.getId(), new Cart(customer2.getId()));
        
        // Initialize empty order lists for customers
        customerOrders.put(customer1.getId(), new ArrayList<>());
        customerOrders.put(customer2.getId(), new ArrayList<>());
    }
    
    // Book operations
    public Book addBook(Book book) {
        if (book.getId() == null) {
            book.setId(bookIdGenerator.getAndIncrement());
        }
        books.put(book.getId(), book);
        return book;
    }
    
    public Book getBook(Long id) {
        return books.get(id);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    public Book updateBook(Book book) {
        books.put(book.getId(), book);
        return book;
    }
    
    public void deleteBook(Long id) {
        books.remove(id);
    }
    
    public List<Book> getBooksByAuthor(Long authorId) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId().equals(authorId)) {
                result.add(book);
            }
        }
        return result;
    }
    
    // Author operations
    public Author addAuthor(Author author) {
        if (author.getId() == null) {
            author.setId(authorIdGenerator.getAndIncrement());
        }
        authors.put(author.getId(), author);
        return author;
    }
    
    public Author getAuthor(Long id) {
        return authors.get(id);
    }
    
    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }
    
    public Author updateAuthor(Author author) {
        authors.put(author.getId(), author);
        return author;
    }
    
    public void deleteAuthor(Long id) {
        authors.remove(id);
    }
    
    // Customer operations
    public Customer addCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(customerIdGenerator.getAndIncrement());
        }
        customers.put(customer.getId(), customer);
        carts.put(customer.getId(), new Cart(customer.getId()));
        customerOrders.put(customer.getId(), new ArrayList<>());
        return customer;
    }
    
    public Customer getCustomer(Long id) {
        return customers.get(id);
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    public Customer updateCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }
    
    public void deleteCustomer(Long id) {
        customers.remove(id);
        carts.remove(id);
        customerOrders.remove(id);
    }
    
    // Cart operations
    public Cart getCart(Long customerId) {
        return carts.get(customerId);
    }
    
    public void updateCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }
    
    public void clearCart(Long customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.clear();
        }
    }
    
    // Order operations
    public Order createOrder(Long customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            return null;
        }
        
        // Create new order from cart
        Order order = new Order();
        order.setId(orderIdGenerator.getAndIncrement());
        order.setCustomerId(customerId);
        
        // Convert cart items to order items with book titles
        for (CartItem cartItem : cart.getItems()) {
            Book book = books.get(cartItem.getBookId());
            if (book != null) {
                OrderItem orderItem = new OrderItem(cartItem, book.getTitle());
                order.addItem(orderItem);
                
                // Reduce book stock
                book.setStock(book.getStock() - cartItem.getQuantity());
            }
        }
        
        // Add order to customer's orders
        List<Order> orders = customerOrders.get(customerId);
        orders.add(order);
        
        // Clear the cart
        cart.clear();
        
        return order;
    }
    
    public List<Order> getCustomerOrders(Long customerId) {
        return customerOrders.getOrDefault(customerId, new ArrayList<>());
    }
    
    public Order getCustomerOrder(Long customerId, Long orderId) {
        List<Order> orders = customerOrders.get(customerId);
        if (orders != null) {
            for (Order order : orders) {
                if (order.getId().equals(orderId)) {
                    return order;
                }
            }
        }
        return null;
    }
}