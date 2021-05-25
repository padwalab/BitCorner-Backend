package org.sjsu.bitcornerbackend.bills;

import java.math.BigDecimal;
import java.util.Date;

import org.sjsu.bitcornerbackend.exceptions.billExceptions.InvalidBillDetails;
import org.sjsu.bitcornerbackend.util.BillStatus;
import org.sjsu.bitcornerbackend.util.Currency;

public class BillBuilder {
    String payerEmail;

    Long payer;

    Long sender;

    String description;

    Currency currency;

    BigDecimal amount;

    Date dueDate;

    BillStatus status;

    public BillBuilder() {
    }

    public BillBuilder setPayer(Long payer) {
        this.payer = payer;
        return this;
    }

    public BillBuilder setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
        return this;
    }

    public BillBuilder setSender(Long sender) {
        this.sender = sender;
        return this;
    }

    public BillBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public BillBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public BillBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public BillBuilder setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public BillBuilder setStatus(BillStatus status) {
        this.status = status;
        return this;
    }

    public Bill build(Long payerId) throws InvalidBillDetails {
        this.status = BillStatus.WAITING;
        if (this.payerEmail == "") {
            throw new InvalidBillDetails("bill must have payer email");
        }
        this.payer = payerId;
        return new Bill(this);
    }

}
