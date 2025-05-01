/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.resources;

import com.bookstore.api.exceptions.AuthorNotFoundException;
import com.bookstore.api.exceptions.InvalidInputException;
import com.bookstore.api.models.Author;
import com.bookstore.api.models.Book;
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
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private final DataStorage dataStorage = DataStorage.getInstance();
    
    @POST
    public Response createAuthor(Author author, @Context UriInfo uriInfo) {
        // Validate author data
        validateAuthorData(author);
        
        // Save the author
        Author savedAuthor = dataStorage.addAuthor(author);
        
        // Create location URI for the new author
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(savedAuthor.getId())).build();
        
        // Return 201 Created with the location header and the author in the body
        return Response.created(location).entity(savedAuthor).build();
    }
    
    @GET
    public Response getAllAuthors() {
        List<Author> authors = dataStorage.getAllAuthors();
        return Response.ok(authors).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") Long id) {
        Author author = dataStorage.getAuthor(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return Response.ok(author).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, Author author) {
        // Check if author exists
        Author existingAuthor = dataStorage.getAuthor(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        
        // Validate author data
        validateAuthorData(author);
        
        // Set the correct ID
        author.setId(id);
        
        // Update the author
        Author updatedAuthor = dataStorage.updateAuthor(author);
        
        return Response.ok(updatedAuthor).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        // Check if author exists
        Author existingAuthor = dataStorage.getAuthor(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        
        // Check if author has books
        List<Book> books = dataStorage.getBooksByAuthor(id);
        if (!books.isEmpty()) {
            throw new InvalidInputException("Cannot delete author with existing books. Remove the books first.");
        }
        
        // Delete the author
        dataStorage.deleteAuthor(id);
        
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{id}/books")
    public Response getAuthorBooks(@PathParam("id") Long id) {
        // Check if author exists
        Author existingAuthor = dataStorage.getAuthor(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        
        // Get books by author
        List<Book> books = dataStorage.getBooksByAuthor(id);
        
        return Response.ok(books).build();
    }
    
    private void validateAuthorData(Author author) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required.");
        }
    }
}
