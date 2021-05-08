package org.sjsu.bitcornerbackend.bankAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.sjsu.bitcornerbackend.util.Currency;

@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @SequenceGenerator(name = "bank_account_sequence", sequenceName = "bank_account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "account_number", nullable = false)
    private Integer accountNumber;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "balance", nullable = false)
    private double balance;

    public BankAccount() {
    }

    public BankAccount(BankAccountBuilder bankAccountBuilder) {
        this.bankName = bankAccountBuilder.bankName;
        this.accountNumber = bankAccountBuilder.accountNumber;
        this.country = bankAccountBuilder.country;
        this.currency = bankAccountBuilder.currency;
        this.balance = bankAccountBuilder.balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankAccount [accountNumber=" + accountNumber + ", balance=" + balance + ", bankName=" + bankName
                + ", country=" + country + ", currency=" + currency + "]";
    }

    // public static class AccountBuilder {
    // private String bankName;
    // private String country;
    // private Integer accountNumber;
    // private Currency currency;
    // private double balance;

    // public AccountBuilder() {

    // }

    // public AccountBuilder setBankName(String bankName) {
    // this.bankName = bankName;
    // return this;
    // }

    // public AccountBuilder setCountry(String country) {
    // this.country = country;
    // return this;
    // }

    // public AccountBuilder setAccountNumber(Integer accountNumber) {
    // this.accountNumber = accountNumber;
    // return this;
    // }

    // public AccountBuilder setCurrency(Currency currency) {
    // this.currency = currency;
    // return this;
    // }

    // public AccountBuilder setBalance(double balance) {
    // this.balance = balance;
    // return this;
    // }

    // public BankAccount build() {
    // return new BankAccount(this);
    // }

    // }
}
