package org.sjsu.bitcornerbackend.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.bankAccount.BankAccountRepository;
import org.sjsu.bitcornerbackend.currencies.Currencies;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.DuplicateNicknameException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.InvalidCredentialsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.orders.Orders;
import org.sjsu.bitcornerbackend.orders.OrdersBuilder;
import org.sjsu.bitcornerbackend.util.CurrencyUnitValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserBuilder userBuilder) {
        User user = userBuilder.build();

        User userResult = userRepository.save(user);
        return userResult;
    }

    @Override
    public User validate(User user) throws UserNotFoundException, InvalidCredentialsException {
        User userResult = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User with email: " + user.getEmail() + " does not exist;"));
        if (!userResult.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Incorrect email or password");
        }
        return userResult;
    }

    @Override
    public User addBankAccount(Long userId, BankAccount bankAccount) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exit"));
        user.setBankAccount(bankAccount);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User update(Long userId, User user) throws UserNotFoundException, InvalidCredentialsException {
        User userOg = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist"));
        if (!userOg.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        userOg.setAddress(user.getAddress());
        userOg.setName(user.getName());

        user = userRepository.save(userOg);

        return user;
    }

    @Override
    public User findById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist"));
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User addOrder(User user, Orders orders) throws BankAccountNotFoundException {
        BankAccount userBankAccount = user.getBankAccount();
        Set<Orders> userOrders = userBankAccount.getOrders();
        userOrders.add(orders);
        userBankAccount.setOrders(userOrders);
        bankAccountRepository.save(userBankAccount);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User initiateOrder(Long userId, OrdersBuilder ordersBuilder)
            throws UserNotFoundException, BankAccountNotFoundException, InsufficientFundsException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist"));
        BigDecimal amount = CurrencyUnitValues.getUnitValue(ordersBuilder.getCurrency(), ordersBuilder.getUnits());
        for (Currencies currencies : user.getBankAccount().getCurrencies()) {
            if (currencies.getCurrency() == ordersBuilder.getCurrency()) {
                if (amount.compareTo(currencies.getAmount()) > 0) {
                    throw new InsufficientFundsException("You don't have enough funds to buy bitcoin");
                }
            }
        }
        return user;
    }

    @Override
    public Boolean checkNickname(String nickname) throws DuplicateNicknameException {

        User user = userRepository.findByNickName(nickname);
        if (user != null) {
            throw new DuplicateNicknameException("Nickname is taken");
        }
        return true;
    }

}
