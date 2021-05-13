package org.sjsu.bitcornerbackend.orders;

import java.math.BigDecimal;
import java.util.Date;

import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.sjsu.bitcornerbackend.util.OrderVariant;

public class OrdersBuilder {
    BigDecimal units;
    Currency currency;

    OrderType type;

    OrderVariant variant;

    BigDecimal limitamt;

    Date createdDate;
    Long user;

    OrderStatus status;

    public BigDecimal getUnits() {
        return units;
    }

    public Currency getCurrency() {
        return currency;
    }

    public OrderType getType() {
        return type;
    }

    public OrderVariant getVariant() {
        return variant;
    }

    public BigDecimal getLimitamt() {
        return limitamt;
    }

    public Long getUser() {
        return user;
    }

    public OrdersBuilder() {
    }

    public OrdersBuilder setUnits(BigDecimal units) {
        this.units = units;
        return this;
    }

    public OrdersBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public OrdersBuilder setType(OrderType type) {
        this.type = type;
        return this;
    }

    public OrdersBuilder setVariant(OrderVariant variant) {
        this.variant = variant;
        return this;
    }

    public OrdersBuilder setLimitamt(BigDecimal limitamt) {
        this.limitamt = limitamt;
        return this;
    }

    public OrdersBuilder setUser(Long user) {
        this.user = user;
        return this;
    }

    public OrdersBuilder setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrdersBuilder setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Orders build() {
        return new Orders(this);
    }
}
