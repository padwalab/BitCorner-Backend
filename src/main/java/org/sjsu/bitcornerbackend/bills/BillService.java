package org.sjsu.bitcornerbackend.bills;

import java.util.List;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.bankAccount.BankAccountRepository;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.InvalidBillDetails;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.SamePayerSenderException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserRepository;
import org.sjsu.bitcornerbackend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BillService implements IBillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Bill> getAllBills() {
        // TODO Auto-generated method stub
        return billRepository.findAll();
    }

    @Override
    public User createBill(BillBuilder billBuilder)
            throws InvalidBillDetails, UserNotFoundException, BankAccountNotFoundException, SamePayerSenderException {
        // TODO Auto-generated method stub
        User payer = userService.findByUserEmail(billBuilder.payerEmail);
        User sender = userService.findById(billBuilder.sender);
        if (payer.getId() == sender.getId()) {
            throw new SamePayerSenderException("can not send bill to yourself");
        }
        Bill bill = billBuilder.build(payer.getBankAccount().getId());
        bill = billRepository.save(bill);
        userService.addBillRecieved(payer.getId(), bill);
        sender = userService.addBillCreated(sender.getId(), bill);

        return sender;
    }
}
