package org.sjsu.bitcornerbackend.orders;

import java.util.List;
import java.util.Set;

import org.sjsu.bitcornerbackend.exceptions.orderExceptions.OrderDoesNotExist;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;

public interface IOrderService {
    List<Orders> all();

    Set<Orders> all(Long id) throws UserNotFoundException;

    List<Orders> findByStatus(OrderStatus status);

    Orders findById(Long orderId) throws OrderDoesNotExist;

    List<Orders> findByStatus(OrderStatus status, OrderType type);

    List<Orders> findByStatus(OrderType type, Currency currency);

    Orders createOrder(OrdersBuilder ordersBuilder);

    List<Orders> allBuys(Long id) throws UserNotFoundException;

    List<Orders> allSells(Long id) throws UserNotFoundException;

}
