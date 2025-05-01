/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.api.models;

import java.util.Objects;

/**
 *
 * @author USER
 */
public class CartItem {
    private Long bookId;
    private int quantity;
    private double price; // Price at time of adding to cart

    // Default constructor
    public CartItem() {
    }

    // Parameterized constructor
    public CartItem(Long bookId, int quantity, double price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity &&
                Double.compare(cartItem.price, price) == 0 &&
                Objects.equals(bookId, cartItem.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, quantity, price);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "bookId=" + bookId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}