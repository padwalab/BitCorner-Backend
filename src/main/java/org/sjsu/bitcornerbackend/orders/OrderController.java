package org.sjsu.bitcornerbackend.orders;

import java.net.URI;
import java.util.Date;
import java.util.List;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;

import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.sjsu.bitcornerbackend.util.OrderVariant;
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

    @GetMapping("/all/{status}/get")
    public ResponseEntity<List<Orders>> getAllOrders(@PathVariable(value = "status") OrderStatus status) {
        return ResponseEntity.ok().body(orderService.findByStatus(status));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<Orders>> getAllOrders(@PathVariable(value = "id") Long userId) {
        return ResponseEntity.ok().body(orderService.all());
    }

    @GetMapping("/bids/open")
    public ResponseEntity<List<Orders>> getOpenBids() {
        return ResponseEntity.ok().body(orderService.findByStatus(OrderStatus.PENDING, OrderType.BUY));
    }

    @GetMapping("/asks/open")
    public ResponseEntity<List<Orders>> getOpenAsks() {
        return ResponseEntity.ok().body(orderService.findByStatus(OrderStatus.PENDING, OrderType.SELL));
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<User> createBuyOrder(@PathVariable(name = "id") Long userId,
            @RequestBody OrdersBuilder ordersBuilder) throws InsufficientFundsException, UserNotFoundException,
            BankAccountNotFoundException, InterruptedException {

        User user = userService.initiateOrder(userId, ordersBuilder);
        ordersBuilder.setType(OrderType.BUY);
        ordersBuilder.setStatus(OrderStatus.PENDING);
        Date now = new Date();
        ordersBuilder.setCreatedDate(now);
        Orders orders = orderService.createOrder(ordersBuilder.setUser(userId));
        user = userService.addOrder(user, orders);

        // orderService.execMarketOrders(orders.getCurrency());
        switch (orders.getVariant()) {
            case LIMIT:
                System.out.println("Thinking about how to handle these orders");
                orderService.execLimitOrders(orders);
                break;
            case MARKET:
                orderService.execMarketOrders(orders.getCurrency());
                break;
            default:
                orderService.execMarketOrders(orders.getCurrency());
                break;
        }

        return ResponseEntity.created(URI.create(String.format("/api/orders/%s", orders.getId()))).body(user);
    }

    @PostMapping("/sell/{id}")
    public ResponseEntity<User> createSellOrder(@PathVariable(name = "id") Long userId,
            @RequestBody OrdersBuilder ordersBuilder)
            throws InsufficientFundsException, UserNotFoundException, BankAccountNotFoundException {

        User user = userService.initiateSellOrder(userId, ordersBuilder.units);
        ordersBuilder.setType(OrderType.SELL);
        ordersBuilder.setStatus(OrderStatus.PENDING);
        Date now = new Date();
        ordersBuilder.setCreatedDate(now);
        Orders orders = orderService.createOrder(ordersBuilder.setUser(userId));
        user = userService.addSellOrder(user, orders);

        switch (orders.getVariant()) {
            case LIMIT:
                System.out.println("Thinking about how to handle these orders");
                orderService.execLimitOrders(orders);
                break;
            case MARKET:
                orderService.execMarketOrders(orders.getCurrency());
                break;
            default:
                orderService.execMarketOrders(orders.getCurrency());
                break;
        }

        return ResponseEntity.created(URI.create(String.format("/api/orders/%s", orders.getId()))).body(user);
    }
}
