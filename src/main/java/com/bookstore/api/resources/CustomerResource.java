/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.resources;

import com.bookstore.api.exceptions.CustomerNotFoundException;
import com.bookstore.api.exceptions.InvalidInputException;
import com.bookstore.api.models.Customer;
import com.bookstore.api.storage.DataStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author USER
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final DataStorage dataStorage = DataStorage.getInstance();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    @POST
    public Response createCustomer(Customer customer, @Context UriInfo uriInfo) {
        // Validate customer data
        validateCustomerData(customer);
        
        // Save the customer
        Customer savedCustomer = dataStorage.addCustomer(customer);
        
        // Create location URI for the new customer
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(savedCustomer.getId())).build();
        
        // Return 201 Created with the location header and the customer in the body
        return Response.created(location).entity(savedCustomer).build();
    }
    
    @GET
    public Response getAllCustomers() {
        List<Customer> customers = dataStorage.getAllCustomers();
        return Response.ok(customers).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        Customer customer = dataStorage.getCustomer(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return Response.ok(customer).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        // Check if customer exists
        Customer existingCustomer = dataStorage.getCustomer(id);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        
        // Validate customer data
        validateCustomerData(customer);
        
        // Set the correct ID
        customer.setId(id);
        
        // Update the customer
        Customer updatedCustomer = dataStorage.updateCustomer(customer);
        
        return Response.ok(updatedCustomer).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        // Check if customer exists
        Customer existingCustomer = dataStorage.getCustomer(id);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        
        // Delete the customer
        dataStorage.deleteCustomer(id);
        
        return Response.noContent().build();
    }
    
    private void validateCustomerData(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required.");
        }
        
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email is required.");
        }
        
        // Validate email format
        if (!EMAIL_PATTERN.matcher(customer.getEmail()).matches()) {
            throw new InvalidInputException("Invalid email format.");
        }
        
        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            throw new InvalidInputException("Customer password is required.");
        }
        
        if (customer.getPassword().length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters long.");
        }
    }
}