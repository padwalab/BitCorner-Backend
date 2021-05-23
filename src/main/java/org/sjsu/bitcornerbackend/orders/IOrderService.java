package org.sjsu.bitcornerbackend.orders;

import java.util.List;
import java.util.Set;

import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;

public interface IOrderService {
    List<Orders> all();

    Set<Orders> all(Long id) throws UserNotFoundException;

    List<Orders> findByStatus(OrderStatus status);
    
    List<Orders> findByStatus(OrderStatus status, OrderType type);

    Orders createOrder(OrdersBuilder ordersBuilder);
}
