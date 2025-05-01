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
public class OrderItem {
    private Long bookId;
    private String title; // Book title at time of purchase
    private int quantity;
    private double price; // Price at time of purchase

    // Default constructor
    public OrderItem() {
    }

    // Parameterized constructor
    public OrderItem(Long bookId, String title, int quantity, double price) {
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    // Convert from CartItem
    public OrderItem(CartItem cartItem, String title) {
        this.bookId = cartItem.getBookId();
        this.title = title;
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getSubtotal() {
        return price * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Double.compare(orderItem.price, price) == 0 &&
                Objects.equals(bookId, orderItem.bookId) &&
                Objects.equals(title, orderItem.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, quantity, price);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}