package org.sjsu.bitcornerbackend.user;

import java.util.List;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.bankAccount.BankAccountBuilder;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.InvalidCredentialsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;

public interface IUserService {
    List<User> listUsers();

    User createUser(UserBuilder userBuilder);

    User validate(User user) throws UserNotFoundException, InvalidCredentialsException;

    User update(Long userId, User user) throws UserNotFoundException, InvalidCredentialsException;

    User addBankAccount(Long userId, BankAccount bankAccount) throws UserNotFoundException;

    User findById(Long userId) throws UserNotFoundException;
}
