package org.sjsu.bitcornerbackend.orders;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.InsufficientFundsException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;

import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.Currency;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/all/buy/{id}")
    public ResponseEntity<List<Orders>> getBuyAllOrders(@PathVariable(value = "id") Long userId)
            throws UserNotFoundException {
        return ResponseEntity.ok().body(orderService.allBuys(userId));
    }

    @GetMapping("/all/sell/{id}")
    public ResponseEntity<List<Orders>> getSellAllOrders(@PathVariable(value = "id") Long userId)
            throws UserNotFoundException {
        return ResponseEntity.ok().body(orderService.allSells(userId));
    }

    @GetMapping("/bids/open")
    public ResponseEntity<List<Orders>> getOpenBids(@RequestParam(name = "currency") Currency currency) {
        return ResponseEntity.ok().body(orderService.findByStatus(OrderType.BUY, currency));
    }

    @GetMapping("/asks/open")
    public ResponseEntity<List<Orders>> getOpenAsks(@RequestParam(name = "currency") Currency currency) {
        return ResponseEntity.ok().body(orderService.findByStatus(OrderType.SELL, currency));
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

        User user = userService.initiateSellOrder(userId, ordersBuilder);
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
