package org.sjsu.bitcornerbackend.bills;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.bankAccount.BankAccountRepository;
import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.currencies.CurrenciesRepository;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.InvalidBillDetails;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.SamePayerSenderException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserRepository;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.BillStatus;
import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.CurrencyUnitValues;
import org.sjsu.bitcornerbackend.util.EURExchangeRate;
import org.sjsu.bitcornerbackend.util.INRExchangeRate;
import org.sjsu.bitcornerbackend.util.USDExchangeRate;
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
    private CurrenciesRepository currenciesRepository;

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
        Bill bill = billBuilder.build(payer.getId());
        bill = billRepository.save(bill);
        userService.addBillRecieved(payer.getId(), bill);
        sender = userService.addBillCreated(sender.getId(), bill);

        return sender;
    }

    @Override
    public Bill cancelBill(Long billId) throws InvalidBillDetails {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new InvalidBillDetails("Bill does not exist"));
        bill.setStatus(BillStatus.CANCELLED);
        billRepository.save(bill);
        return bill;
    }

    @Override
    public Bill rejectBill(Long billId) throws InvalidBillDetails {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new InvalidBillDetails("Bill does not exist"));
        bill.setStatus(BillStatus.REJECTED);
        billRepository.save(bill);
        return bill;
    }

    public Bill paySameCurrency(User payer, User sender, Bill bill, Currency currency)
            throws InsufficientFundsException {
        Currencies payerCurrencies;
        for (Currencies currencies : payer.getBankAccount().getCurrencies()) {
            if (currencies.getCurrency() == currency) {
                if (currencies.getAmount().compareTo(bill.getAmount()) < 0) {
                    throw new InsufficientFundsException("User does not have enough funds to pay the bill");
                }
                payerCurrencies = currencies;
                for (Currencies currencies2 : sender.getBankAccount().getCurrencies()) {
                    if (currencies2.getCurrency() == currency) {
                        BigDecimal finalBal = payerCurrencies.getAmount().subtract(bill.getAmount());
                        payerCurrencies.setAmount(finalBal);
                        BigDecimal finalSenderBal = currencies2.getAmount().add(bill.getAmount());
                        currencies2.setAmount(finalSenderBal);
                        currenciesRepository.save(currencies2);
                        currenciesRepository.save(payerCurrencies);
                        bill.setStatus(BillStatus.PAID);
                    }
                }
            }
        }
        return bill;
    }

    public Bill payUsingBTC(User payer, User sender, Bill bill, Currency currency) throws InsufficientFundsException {
        Currencies payerCurrencies;
        BigDecimal unitAmtWithTransactionFee = CurrencyUnitValues.getUnitValue(bill.getCurrency(), new BigDecimal(1))
                .multiply(new BigDecimal(1.05));
        BigDecimal deductionAmount = bill.getAmount().divide(unitAmtWithTransactionFee, 7, RoundingMode.DOWN)
                .multiply(new BigDecimal(1.0001));
        for (Currencies currencies : payer.getBankAccount().getCurrencies()) {
            if (currencies.getCurrency() == currency) {
                if (currencies.getAmount().compareTo(deductionAmount) < 0) {
                    throw new InsufficientFundsException("User does not have enough BTC to pay the bill\n BTC To pay: "
                            + deductionAmount.toString());
                }
                payerCurrencies = currencies;
                for (Currencies currencies2 : sender.getBankAccount().getCurrencies()) {
                    if (currencies2.getCurrency() == bill.getCurrency()) {
                        BigDecimal finalBal = payerCurrencies.getAmount().subtract(deductionAmount);
                        payerCurrencies.setAmount(finalBal);
                        BigDecimal finalSenderBal = currencies2.getAmount().add(bill.getAmount());
                        // See how to transfer the admin fee to admin account
                        currencies2.setAmount(finalSenderBal);
                        currenciesRepository.save(currencies2);
                        currenciesRepository.save(payerCurrencies);
                        bill.setStatus(BillStatus.PAID);
                    }
                }
            }
        }
        return bill;
    }

    public Bill payDiffCurrency(User payer, User sender, Bill bill, Currency currency)
            throws InsufficientFundsException {
        Currencies payerCurrencies;
        BigDecimal deductionAmount = new BigDecimal(0);
        switch (bill.getCurrency()) {
            case USD:
                deductionAmount = USDExchangeRate.getUSDUnitValue(currency, bill.getAmount())
                        .multiply(new BigDecimal(1.0001));
                break;
            case EUR:
                deductionAmount = EURExchangeRate.getEURUnitValue(currency, bill.getAmount())
                        .multiply(new BigDecimal(1.0001));
                break;
            case GBP:
                deductionAmount = USDExchangeRate.getUSDUnitValue(currency, bill.getAmount())
                        .multiply(new BigDecimal(1.0001));
                break;
            case INR:
                deductionAmount = INRExchangeRate.getINRUnitValue(currency, bill.getAmount())
                        .multiply(new BigDecimal(1.0001));
                break;
        }

        for (Currencies currencies : payer.getBankAccount().getCurrencies()) {
            if (currencies.getCurrency() == currency) {
                if (currencies.getAmount().compareTo(deductionAmount) < 0) {
                    throw new InsufficientFundsException(
                            "User does not have enough funds to pay the bill\n BTC To pay: " + currency
                                    + deductionAmount.toString());
                }
                payerCurrencies = currencies;
                for (Currencies currencies2 : sender.getBankAccount().getCurrencies()) {
                    if (currencies2.getCurrency() == bill.getCurrency()) {
                        BigDecimal finalBal = payerCurrencies.getAmount().subtract(deductionAmount);
                        payerCurrencies.setAmount(finalBal);
                        BigDecimal finalSenderBal = currencies2.getAmount().add(bill.getAmount());
                        // See how to transfer the admin fee to admin account
                        currencies2.setAmount(finalSenderBal);
                        currenciesRepository.save(currencies2);
                        currenciesRepository.save(payerCurrencies);
                        bill.setStatus(BillStatus.PAID);
                    }
                }
            }
        }
        return bill;
    }

    @Override
    public Bill payBill(Long billId, Currency currency)
            throws InvalidBillDetails, UserNotFoundException, InsufficientFundsException {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new InvalidBillDetails("Bill does not exist"));
        User sender = userService.findById(bill.getSender());
        User payer = userService.findById(bill.getpayer());
        System.out.println(bill.toString() + "\nreciever" + sender.toString() + "\npayer" + payer.toString());

        if (currency == bill.getCurrency()) {
            bill = paySameCurrency(payer, sender, bill, currency);
            billRepository.save(bill);
            return bill;
        } else if (currency == Currency.BTC) {
            bill = payUsingBTC(payer, sender, bill, currency);
            billRepository.save(bill);
            return bill;
        } else if (currency != bill.getCurrency()) {
            bill = payDiffCurrency(payer, sender, bill, currency);
            billRepository.save(bill);
            return bill;
        }

        return null;
    }
}
