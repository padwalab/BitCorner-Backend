package org.sjsu.bitcornerbackend.orders;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.sjsu.bitcornerbackend.util.OrderVariant;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @SequenceGenerator(name = "orders_sequence", sequenceName = "orders_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "units")
    private BigDecimal units;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "type")
    private OrderType type;

    @Column(name = "variant")
    private OrderVariant variant;

    @Column(name = "limitamt")
    private BigDecimal limitamt;

    @Column(name = "userid")
    private Long user;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_date")
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Orders() {
    }

    public Orders(long id, BigDecimal units, OrderType type, OrderVariant variant, BigDecimal limitamt, Long user,
            OrderStatus status, Date createdDate, Date lastModifiedDate) {
        this.id = id;
        this.units = units;
        this.type = type;
        this.variant = variant;
        this.limitamt = limitamt;
        this.user = user;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Orders(OrdersBuilder ordersBuilder) {
        this.user = ordersBuilder.user;
        this.limitamt = ordersBuilder.limitamt;
        this.type = ordersBuilder.type;
        this.variant = ordersBuilder.variant;
        this.units = ordersBuilder.units;
        this.currency = ordersBuilder.currency;
        this.status = ordersBuilder.status;
        this.createdDate = ordersBuilder.createdDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderVariant getVariant() {
        return variant;
    }

    public void setVariant(OrderVariant variant) {
        this.variant = variant;
    }

    public BigDecimal getLimitamt() {
        return limitamt;
    }

    public void setLimitamt(BigDecimal limitamt) {
        this.limitamt = limitamt;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Orders [id=" + id + ", limitamt=" + limitamt + ", type=" + type + ", units=" + units + ", variant="
                + variant + "]";
    }

}
