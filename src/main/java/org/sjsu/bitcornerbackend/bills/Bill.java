package org.sjsu.bitcornerbackend.bills;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.sjsu.bitcornerbackend.util.BillStatus;
import org.sjsu.bitcornerbackend.util.Currency;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @SequenceGenerator(name = "bill_sequence", sequenceName = "bill_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "payer", nullable = false)
    private Long payer;

    @Column(name = "sender", nullable = false)
    private Long sender;

    @Column(name = "description")
    private String description;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "status")
    private BillStatus status;

    public Bill() {

    }

    public Bill(BillBuilder billBuilder) {
        this.payer = billBuilder.payer;
        this.sender = billBuilder.sender;
        this.description = billBuilder.description;
        this.currency = billBuilder.currency;
        this.amount = billBuilder.amount;
        this.dueDate = billBuilder.dueDate;
        this.status = billBuilder.status;
    }

    public Bill(long id, Long payer, Long sender, String description, Currency currency, BigDecimal amount,
            Date dueDate, BillStatus status) {
        this.id = id;
        this.payer = payer;
        this.sender = sender;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getpayer() {
        return payer;
    }

    public void setpayer(Long payer) {
        this.payer = payer;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bill [amount=" + amount + ", currency=" + currency + ", description=" + description + ", dueDate="
                + dueDate + ", id=" + id + ", payer=" + payer + ", sender=" + sender + ", status=" + status + "]";
    }

}
