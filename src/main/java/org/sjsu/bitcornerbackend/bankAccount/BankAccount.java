package org.sjsu.bitcornerbackend.bankAccount;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import javax.persistence.Table;

import org.sjsu.bitcornerbackend.bills.Bill;
import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.orders.Orders;
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

    @OneToMany
    private Set<Currencies> currencies;

    @OneToMany
    private Set<Orders> orders;

    @OneToMany
    private List<Bill> bills;

    @OneToMany
    private List<Bill> recievedBills;

    public BankAccount() {
    }

    public BankAccount(BankAccountBuilder bankAccountBuilder) {
        this.bankName = bankAccountBuilder.bankName;
        this.accountNumber = bankAccountBuilder.accountNumber;
        this.country = bankAccountBuilder.country;
        this.currency = bankAccountBuilder.currency;
        this.currencies = bankAccountBuilder.currencies;
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

    public Set<Currencies> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Set<Currencies> currencies) {
        this.currencies = currencies;
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Bill> getRecievedBills() {
        return recievedBills;
    }

    public void setRecievedBills(List<Bill> recievedBills) {
        this.recievedBills = recievedBills;
    }

    @Override
    public String toString() {
        return "BankAccount [accountNumber=" + accountNumber + ", bankName=" + bankName + ", country=" + country
                + ", currency=" + currency + "]";
    }

}
