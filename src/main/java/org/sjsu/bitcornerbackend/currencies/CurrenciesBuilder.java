package org.sjsu.bitcornerbackend.currencies;

import java.math.BigDecimal;

import org.sjsu.bitcornerbackend.util.Currency;

public class CurrenciesBuilder {
    long id;
    Currency currency;
    BigDecimal amount;
    BigDecimal hold;

    public CurrenciesBuilder() {

    }

    public CurrenciesBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public CurrenciesBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public CurrenciesBuilder setHold(BigDecimal hold) {
        this.hold = hold;
        return this;
    }

    public Currencies build() {
        return new Currencies(this);
    }

}
