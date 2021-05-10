package org.sjsu.bitcornerbackend.orders;

import java.math.BigDecimal;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.sjsu.bitcornerbackend.util.OrderVariant;

public class OrdersBuilder {
    int units;

    OrderType type;

    OrderVariant variant;

    BigDecimal limitamt;

    BankAccount bankAccount;

    public OrdersBuilder() {
    }

    public OrdersBuilder setUnits(int units) {
        this.units = units;
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

    public OrdersBuilder setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public Orders build() {
        return new Orders(this);
    }
}
