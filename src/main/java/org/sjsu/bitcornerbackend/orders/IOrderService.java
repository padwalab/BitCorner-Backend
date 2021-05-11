package org.sjsu.bitcornerbackend.orders;

import java.util.List;

public interface IOrderService {
    List<Orders> all();

    Orders createOrder(OrdersBuilder ordersBuilder);
}
