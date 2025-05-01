/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.resources;

import com.bookstore.api.exceptions.AuthorNotFoundException;
import com.bookstore.api.exceptions.BookNotFoundException;
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
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author USER
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    
    private final DataStorage dataStorage = DataStorage.getInstance();
    
    @POST
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        // Validate book data
        validateBookData(book);
        
        // Check if author exists
        Author author = dataStorage.getAuthor(book.getAuthorId());
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        
        // Save the book
        Book savedBook = dataStorage.addBook(book);
        
        // Create location URI for the new book
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(savedBook.getId())).build();
        
        // Return 201 Created with the location header and the book in the body
        return Response.created(location).entity(savedBook).build();
    }
    
    @GET
    public Response getAllBooks() {
        List<Book> books = dataStorage.getAllBooks();
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        Book book = dataStorage.getBook(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.ok(book).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        // Check if book exists
        Book existingBook = dataStorage.getBook(id);
        if (existingBook == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        
        // Validate book data
        validateBookData(book);
        
        // Check if author exists
        Author author = dataStorage.getAuthor(book.getAuthorId());
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        
        // Set the correct ID
        book.setId(id);
        
        // Update the book
        Book updatedBook = dataStorage.updateBook(book);
        
        return Response.ok(updatedBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        // Check if book exists
        Book existingBook = dataStorage.getBook(id);
        if (existingBook == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        
        // Delete the book
        dataStorage.deleteBook(id);
        
        return Response.noContent().build();
    }
    
    private void validateBookData(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title is required.");
        }
        
        if (book.getAuthorId() == null) {
            throw new InvalidInputException("Author ID is required.");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN is required.");
        }
        
        int currentYear = LocalDate.now().getYear();
        if (book.getPublicationYear() > currentYear) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero.");
        }
        
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative.");
        }
    }
}