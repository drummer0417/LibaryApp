package com.library.app.order.model;

import com.library.app.book.model.Book;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 3520003746949600860L;

    @NotNull
    @JoinColumn(name = "book_id")
    @ManyToOne
    private Book book;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double price;

    public OrderItem() {

    }

    public OrderItem(@NotNull Book book, @NotNull Integer quantity) {

        this.book = book;
        this.quantity = quantity;
    }

    public void calculatePrice() {

        if (book != null && quantity != null) {
            price = book.getPrice() * quantity;
        }
    }

    @NotNull
    public Book getBook() {

        return book;
    }

    public void setBook(@NotNull Book book) {

        this.book = book;
    }

    @NotNull
    public Integer getQuantity() {

        return quantity;
    }

    public void setQuantity(@NotNull Integer quantity) {

        this.quantity = quantity;
    }

    @NotNull
    public Double getPrice() {

        return price;
    }

    public void setPrice(@NotNull Double price) {

        this.price = price;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (!book.equals(orderItem.book)) return false;
        if (quantity != null ? !quantity.equals(orderItem.quantity) : orderItem.quantity != null) return false;
        return price != null ? price.equals(orderItem.price) : orderItem.price == null;
    }

    @Override
    public int hashCode() {

        return book.hashCode();
    }

    @Override
    public String toString() {

        return "OrderItem{" +
                "book=" + book +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
