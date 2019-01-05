package com.library.app.order.model;

import com.library.app.book.model.Book;
import com.library.app.user.model.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lib_order")
public class Order implements Serializable {

    private static final long serialVersionUID = -8989259839221155209L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;


    @NotNull
    @ManyToMany
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    private Double total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private OrderStatus currentStatus;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lib_order_item", joinColumns = @JoinColumn(name = "order_id"))
    @NotNull
    @Size(min = 1)
    private Set<OrderItem> items;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lib_order_history", joinColumns = @JoinColumn(name = "order_id"))
    @NotNull
    private Set<OrederHistoryEntry> orderHistoryEntry;

    public Order() {

        this.createdAt = new Date();
    }

    public boolean add(Book book, Integer quantity) {

        OrderItem orderItem = new OrderItem(book, quantity);
        return getItems().add(orderItem);
    }

    public void calculateTotal() {

        this.total = 0D;
        for (OrderItem item : items) {
            item.calculatePrice();
            total += item.getPrice();
        }
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @NotNull
    public Date getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(@NotNull Date createdAt) {

        this.createdAt = createdAt;
    }

    @NotNull
    public Customer getCustomer() {

        return customer;
    }

    public void setCustomer(@NotNull Customer customer) {

        this.customer = customer;
    }

    @NotNull
    public Double getTotal() {

        return total;
    }

    public void setTotal(@NotNull Double total) {

        this.total = total;
    }

    @NotNull
    public OrderStatus getCurrentStatus() {

        return currentStatus;
    }

    public void setCurrentStatus(@NotNull OrderStatus currentStatus) {

        this.currentStatus = currentStatus;
    }

    @NotNull
    public Set<OrderItem> getItems() {

        if (items == null) {
            items = new HashSet<>();
        }
        return items;
    }

    public void setItems(@NotNull Set<OrderItem> items) {

        this.items = items;
    }

    @NotNull
    public Set<OrederHistoryEntry> getOrderHistoryEntry() {

        if (orderHistoryEntry == null) {
            orderHistoryEntry = new HashSet<>();
        }
        return orderHistoryEntry;
    }

    public void setOrderHistoryEntry(@NotNull Set<OrederHistoryEntry> orderHistoryEntry) {

        this.orderHistoryEntry = orderHistoryEntry;
    }

    @Override
    public String toString() {

        return "Order{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", customer=" + customer +
                ", total=" + total +
                ", currentStatus=" + currentStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id != null ? id.equals(order.id) : order.id == null;
    }

    @Override
    public int hashCode() {

        return id != null ? id.hashCode() : 0;
    }

    public enum OrderStatus {
        RESERVED, RESERVATION_EXPIRED, DELIVERED, CANCELLED;

    }
}
