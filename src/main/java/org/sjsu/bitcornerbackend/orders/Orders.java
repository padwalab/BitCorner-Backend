package org.sjsu.bitcornerbackend.orders;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;
import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
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
    private int units;

    @Column(name = "type")
    private OrderType type;

    @Column(name = "variant")
    private OrderVariant variant;

    @Column(name = "limitamt")
    private BigDecimal limitamt;

    @ManyToOne
    private BankAccount bankAccount;

    public Orders() {
    }

    public Orders(long id, int units, OrderType type, OrderVariant variant, BigDecimal limitamt,
            BankAccount bankAccount) {
        this.id = id;
        this.units = units;
        this.type = type;
        this.variant = variant;
        this.limitamt = limitamt;
        this.bankAccount = bankAccount;
    }

    public Orders(OrdersBuilder ordersBuilder) {
        this.bankAccount = ordersBuilder.bankAccount;
        this.limitamt = ordersBuilder.limitamt;
        this.type = ordersBuilder.type;
        this.variant = ordersBuilder.variant;
        this.units = ordersBuilder.units;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String toString() {
        return "Orders [id=" + id + ", limitamt=" + limitamt + ", type=" + type + ", units=" + units + ", variant="
                + variant + "]";
    }

}
