package org.sjsu.bitcornerbackend.bankAccount;

import java.math.BigDecimal;

import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.util.Currency;

public interface IBankAccountService {
    BankAccount createBankAccount(BankAccountBuilder bankAccountBuilder);

    User deposit(long userId, Currency currency, BigDecimal amount) throws UserNotFoundException;

    User withdraw(long userId, Currency currency, BigDecimal amount)
            throws UserNotFoundException, InsufficientFundsException;
}
