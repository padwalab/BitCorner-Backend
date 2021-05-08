package org.sjsu.bitcornerbackend.user;

import java.util.List;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.bankAccount.BankAccountBuilder;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.InvalidCredentialsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User.UserBuilder;

public interface IUserService {
    List<User> listUsers();

    User createUser(UserBuilder userBuilder);

    User validate(User user) throws UserNotFoundException, InvalidCredentialsException;

    User addBankAccount(Long userId, BankAccount bankAccount) throws UserNotFoundException;
}
