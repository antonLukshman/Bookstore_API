/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.resources;

import com.bookstore.api.exceptions.BookNotFoundException;
import com.bookstore.api.exceptions.CartNotFoundException;
import com.bookstore.api.exceptions.CustomerNotFoundException;
import com.bookstore.api.exceptions.InvalidInputException;
import com.bookstore.api.exceptions.OutOfStockException;
import com.bookstore.api.models.Book;
import com.bookstore.api.models.Cart;
import com.bookstore.api.models.CartItem;
import com.bookstore.api.models.Customer;
import com.bookstore.api.storage.DataStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author USER
 */
@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private final DataStorage dataStorage = DataStorage.getInstance();
    
    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") Long customerId, CartItem cartItem) {
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Validate book exists
        Book book = dataStorage.getBook(cartItem.getBookId());
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + cartItem.getBookId() + " does not exist.");
        }
        
        // Validate quantity
        if (cartItem.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }
        
        // Check stock
        if (book.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException("Not enough stock for book with ID " + cartItem.getBookId() + 
                ". Available: " + book.getStock() + ", Requested: " + cartItem.getQuantity());
        }
        
        // Set the price from the book (to prevent client from setting arbitrary prices)
        cartItem.setPrice(book.getPrice());
        
        // Get or create cart for customer
        Cart cart = dataStorage.getCart(customerId);
        if (cart == null) {
            cart = new Cart(customerId);
        }
        
        // Add item to cart
        cart.addItem(cartItem);
        
        // Update cart in storage
        dataStorage.updateCart(cart);
        
        return Response.ok(cart).build();
    }
    
    @GET
    public Response getCart(@PathParam("customerId") Long customerId) {
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Get cart
        Cart cart = dataStorage.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist.");
        }
        
        return Response.ok(cart).build();
    }
    
    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(
            @PathParam("customerId") Long customerId,
            @PathParam("bookId") Long bookId,
            CartItem cartItem) {
        
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Validate book exists
        Book book = dataStorage.getBook(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }
        
        // Make sure cartItem.bookId matches the path parameter
        cartItem.setBookId(bookId);
        
        // Validate quantity
        if (cartItem.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }
        
        // Check stock
        if (book.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException("Not enough stock for book with ID " + bookId + 
                ". Available: " + book.getStock() + ", Requested: " + cartItem.getQuantity());
        }
        
        // Get cart
        Cart cart = dataStorage.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist.");
        }
        
        // Check if item exists in cart
        boolean itemExists = cart.getItems().stream()
                .anyMatch(item -> item.getBookId().equals(bookId));
        
        if (!itemExists) {
            throw new InvalidInputException("Book with ID " + bookId + " not found in cart.");
        }
        
        // Set the price from the book
        cartItem.setPrice(book.getPrice());
        
        // Update item in cart
        cart.updateItem(bookId, cartItem.getQuantity());
        
        // Update cart in storage
        dataStorage.updateCart(cart);
        
        return Response.ok(cart).build();
    }
    
    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(
            @PathParam("customerId") Long customerId,
            @PathParam("bookId") Long bookId) {
        
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Get cart
        Cart cart = dataStorage.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist.");
        }
        
        // Remove item from cart
        cart.removeItem(bookId);
        
        // Update cart in storage
        dataStorage.updateCart(cart);
        
        return Response.noContent().build();
    }
}
