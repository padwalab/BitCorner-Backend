package org.sjsu.bitcornerbackend.currencies;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.sjsu.bitcornerbackend.util.Currency;

@Entity
@Table(name = "currencies")
public class Currencies {

    @Id
    @SequenceGenerator(name = "currencies_sequence", sequenceName = "currencies_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currencies_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "balance", nullable = false)
    private BigDecimal amount;

    @Column(name = "hold")
    private BigDecimal hold;

    public Currencies() {
    }

    public Currencies(long id, Currency currency, BigDecimal amount, BigDecimal hold) {
        this.id = id;
        this.currency = currency;
        this.amount = amount;
        this.hold = hold;
    }

    public Currencies(CurrenciesBuilder currenciesBuilder) {
        this.currency = currenciesBuilder.currency;
        this.amount = currenciesBuilder.amount;
        this.hold = currenciesBuilder.hold;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getHold() {
        return hold;
    }

    public void setHold(BigDecimal hold) {
        this.hold = hold;
    }

    @Override
    public String toString() {
        return "Currencies [amount=" + amount + ", currency=" + currency + ", id=" + id + "]";
    }

}
