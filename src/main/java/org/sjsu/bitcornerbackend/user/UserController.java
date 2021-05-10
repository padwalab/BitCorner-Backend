package org.sjsu.bitcornerbackend.user;

import java.net.URI;
import java.util.List;

import org.sjsu.bitcornerbackend.exceptions.userExceptions.InvalidCredentialsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.listUsers();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserBuilder userBuilder) {
        User user = userService.createUser(userBuilder);
        return ResponseEntity.created(URI.create(String.format("/api/users/%s", user.getId()))).body(user);
    }

    @PostMapping(value = "/validate")
    public ResponseEntity<User> validateUser(@RequestBody User user)
            throws UserNotFoundException, InvalidCredentialsException {
        User userResult = userService.validate(user);
        return ResponseEntity.ok().body(userResult);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exit"));
        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable(value = "id") Long userId, @RequestBody User user)
            throws UserNotFoundException, InvalidCredentialsException {
        User userResult = userService.update(userId, user);
        return ResponseEntity.ok().body(userResult);
    }
}
