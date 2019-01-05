package com.library.app.order.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class OrederHistoryEntry implements Serializable {

    private static final long serialVersionUID = 1302541678090421773L;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Order.OrderStatus orderStatus;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    public OrederHistoryEntry() {

    }

    public OrederHistoryEntry(@NotNull Order.OrderStatus orderStatus) {

        this.createdAt = new Date();
        this.orderStatus = orderStatus;
    }

    @NotNull
    public Order.OrderStatus getOrderStatus() {

        return orderStatus;
    }

    public void setOrderStatus(@NotNull Order.OrderStatus orderStatus) {

        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrederHistoryEntry that = (OrederHistoryEntry) o;

        if (orderStatus != that.orderStatus) return false;
        return createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {

        int result = orderStatus.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

    @NotNull
    public Date getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(@NotNull Date createdAt) {

        this.createdAt = createdAt;
    }

    @Override
    public String toString() {

        return "OrederHistoryEntry{" +
                "orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
