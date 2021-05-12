package org.sjsu.bitcornerbackend.orders;

import java.util.List;
import java.util.Set;

import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;

public interface IOrderService {
    List<Orders> all();

    Set<Orders> all(Long id) throws UserNotFoundException;

    Orders createOrder(OrdersBuilder ordersBuilder);
}
