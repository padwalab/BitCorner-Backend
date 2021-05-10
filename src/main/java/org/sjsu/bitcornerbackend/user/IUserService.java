package org.sjsu.bitcornerbackend.user;

import java.util.List;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.InvalidCredentialsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.orders.Orders;
import org.sjsu.bitcornerbackend.orders.OrdersBuilder;

public interface IUserService {
    List<User> listUsers();

    User createUser(UserBuilder userBuilder);

    User validate(User user) throws UserNotFoundException, InvalidCredentialsException;

    User update(Long userId, User user) throws UserNotFoundException, InvalidCredentialsException;

    User addBankAccount(Long userId, BankAccount bankAccount) throws UserNotFoundException;

    User findById(Long userId) throws UserNotFoundException;

    void saveUser(User user);

    User addOrder(User user, Orders orders) throws BankAccountNotFoundException;

    User initiateOrder(Long userId, OrdersBuilder ordersBuilder)
            throws UserNotFoundException, BankAccountNotFoundException, InsufficientFundsException;
}
