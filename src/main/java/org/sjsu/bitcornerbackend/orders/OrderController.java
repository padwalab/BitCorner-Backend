package org.sjsu.bitcornerbackend.orders;

import java.net.URI;
import java.util.List;

import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.all());
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<User> createBuyOrder(@PathVariable(name = "id") Long userId,
            @RequestBody OrdersBuilder ordersBuilder)
            throws InsufficientFundsException, UserNotFoundException, BankAccountNotFoundException {

        User user = userService.initiateOrder(userId, ordersBuilder);
        ordersBuilder.setType(OrderType.BUY);
        Orders orders = orderService.createOrder(ordersBuilder.setUser(userId));
        user = userService.addOrder(user, orders);

        return ResponseEntity.created(URI.create(String.format("/api/orders/%s", orders.getId()))).body(user);
    }

    @PostMapping("/sell/{id}")
    public ResponseEntity<User> createSellOrder(@PathVariable(name = "id") Long userId,
            @RequestBody OrdersBuilder ordersBuilder)
            throws InsufficientFundsException, UserNotFoundException, BankAccountNotFoundException {

        User user = userService.initiateSellOrder(userId, ordersBuilder.units);
        ordersBuilder.setType(OrderType.SELL);
        Orders orders = orderService.createOrder(ordersBuilder.setUser(userId));
        user = userService.addOrder(user, orders);

        return ResponseEntity.created(URI.create(String.format("/api/orders/%s", orders.getId()))).body(user);
    }
}
