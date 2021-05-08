package org.sjsu.bitcornerbackend.bankAccount;

import org.sjsu.bitcornerbackend.util.Currency;

public class BankAccountBuilder {
    String bankName;
    String country;
    Integer accountNumber;
    Currency currency;
    double balance;

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

    public BankAccountBuilder setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public BankAccount build() {
        return new BankAccount(this);
    }

}
