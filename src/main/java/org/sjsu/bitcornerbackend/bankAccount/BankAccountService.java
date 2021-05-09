package org.sjsu.bitcornerbackend.bankAccount;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.currencies.CurrenciesRepository;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserRepository;
import org.sjsu.bitcornerbackend.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankAccountService implements IBankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Override
    public BankAccount createBankAccount(BankAccountBuilder bankAccountBuilder) {
        BankAccount bankAccount = bankAccountBuilder.build();
        BankAccount bankAccountResult = bankAccountRepository.save(bankAccount);
        return bankAccountResult;
    }

    @Override
    public User deposit(long userId, Currency currency, BigDecimal amount) throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist"));

        BankAccount uBankAccount = user.getBankAccount();

        for (Currencies currencies : uBankAccount.getCurrencies()) {
            if (currencies.getCurrency() == currency) {
                currencies.setAmount(currencies.getAmount().add(amount));
                currenciesRepository.save(currencies);
            }
        }
        // uBankAccount.setBalance(uBankAccount.getBalance().add(amount));

        uBankAccount = bankAccountRepository.save(uBankAccount);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User withdraw(long userId, Currency currency, BigDecimal amount) throws UserNotFoundException, InsufficientFundsException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist"));

        BankAccount uBankAccount = user.getBankAccount();

        for (Currencies currencies : uBankAccount.getCurrencies()) {
            if (currencies.getCurrency() == currency ) {
                if (currencies.getAmount().compareTo(amount) < 0){
                    throw new InsufficientFundsException("Insufficient funds...");
                }
                currencies.setAmount(currencies.getAmount().subtract(amount));
                currenciesRepository.save(currencies);
            }
        }
        // uBankAccount.setBalance(uBankAccount.getBalance().add(amount));

        uBankAccount = bankAccountRepository.save(uBankAccount);
        user = userRepository.save(user);
        return user;
    }

}
