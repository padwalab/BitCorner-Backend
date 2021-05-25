package org.sjsu.bitcornerbackend.bills;

import java.util.List;

import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.InvalidBillDetails;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.SamePayerSenderException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;

public interface IBillService {
    public List<Bill> getAllBills();

    public User createBill(BillBuilder billBuilder)
            throws InvalidBillDetails, UserNotFoundException, BankAccountNotFoundException, SamePayerSenderException;
}
