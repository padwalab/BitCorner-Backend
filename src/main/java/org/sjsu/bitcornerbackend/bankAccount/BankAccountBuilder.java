package org.sjsu.bitcornerbackend.bankAccount;

import java.math.BigDecimal;
import java.util.Set;

import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.util.Currency;

public class BankAccountBuilder {
    String bankName;
    String country;
    Integer accountNumber;
    Currency currency;
    Set<Currencies> currencies;

    public BankAccountBuilder() {

    }

    public BankAccountBuilder setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public BankAccountBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public BankAccountBuilder setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public BankAccountBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public BankAccountBuilder setCurruncies(Set<Currencies> currencies) {
        this.currencies = currencies;
        return this;
    }

    public BankAccount build() {
        return new BankAccount(this);
    }

}
