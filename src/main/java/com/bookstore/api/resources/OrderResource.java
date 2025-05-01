/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.resources;

import com.bookstore.api.exceptions.CartNotFoundException;
import com.bookstore.api.exceptions.CustomerNotFoundException;
import com.bookstore.api.exceptions.InvalidInputException;
import com.bookstore.api.models.Cart;
import com.bookstore.api.models.Customer;
import com.bookstore.api.models.Order;
import com.bookstore.api.storage.DataStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 *
 * @author USER
 */
@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final DataStorage dataStorage = DataStorage.getInstance();
    
    @POST
    public Response createOrder(@PathParam("customerId") Long customerId, @Context UriInfo uriInfo) {
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Get cart
        Cart cart = dataStorage.getCart(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " is empty or does not exist.");
        }
        
        // Create order from cart
        Order order = dataStorage.createOrder(customerId);
        if (order == null) {
            throw new InvalidInputException("Failed to create order. Please check cart contents.");
        }
        
        // Create location URI for the new order
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(order.getId())).build();
        
        // Return 201 Created with the location header and the order in the body
        return Response.created(location).entity(order).build();
    }
    
    @GET
    public Response getCustomerOrders(@PathParam("customerId") Long customerId) {
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Get customer orders
        List<Order> orders = dataStorage.getCustomerOrders(customerId);
        
        return Response.ok(orders).build();
    }
    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") Long customerId, @PathParam("orderId") Long orderId) {
        // Validate customer exists
        Customer customer = dataStorage.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        
        // Get order
        Order order = dataStorage.getCustomerOrder(customerId, orderId);
        if (order == null) {
            throw new InvalidInputException("Order with ID " + orderId + " not found for customer with ID " + customerId);
        }
        
        return Response.ok(order).build();
    }
}