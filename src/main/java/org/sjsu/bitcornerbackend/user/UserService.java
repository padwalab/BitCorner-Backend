package org.sjsu.bitcornerbackend.user;

import java.util.List;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.InvalidCredentialsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

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

}
