package org.sjsu.bitcornerbackend.bankAccount;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Set;

import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.currencies.CurrenciesService;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CurrenciesService currenciesService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/{id}")
    public ResponseEntity<User> addBankAccount(@PathVariable(value = "id") Long userId,
            @RequestBody BankAccountBuilder bankAccountBuilder) throws UserNotFoundException {
        Set<Currencies> defaultCurrencies = currenciesService.createDefaultCurrencies();
        bankAccountBuilder.setCurruncies(defaultCurrencies);
        BankAccount bankAccount = bankAccountService.createBankAccount(bankAccountBuilder);

        User user = userService.addBankAccount(userId, bankAccount);

        return ResponseEntity.created(URI.create(String.format("/api/%s/%s", user.getId(), bankAccount.getId())))
                .body(user);
    }

    @PutMapping(value = "/deposit/{id}")
    public ResponseEntity<User> depositToAccount(@PathVariable(value = "id") Long userId,
            @RequestParam(value = "amount", required = true) BigDecimal amount,
            @RequestParam(value = "currency", required = true) Currency currency) throws UserNotFoundException {

        User user = bankAccountService.deposit(userId, currency, amount);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BankAccount> getBankAccount(@PathVariable(value = "id") Long userId)
            throws UserNotFoundException, BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountService.getBankAccountForUserId(userId);

        return ResponseEntity.ok().body(bankAccount);
    }

    @PutMapping(value = "/withdraw/{id}")
    public ResponseEntity<User> withdrawFromAccount(@PathVariable(value = "id") Long userId,
            @RequestParam(value = "amount", required = true) BigDecimal amount,
            @RequestParam(value = "currency", required = true) Currency currency)
            throws UserNotFoundException, InsufficientFundsException {

        User user = bankAccountService.withdraw(userId, currency, amount);

        return ResponseEntity.ok().body(user);
    }
}
