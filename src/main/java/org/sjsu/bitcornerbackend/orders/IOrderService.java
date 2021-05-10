package org.sjsu.bitcornerbackend.orders;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

public interface IOrderService {
    List<Orders> all();
    Orders createOrder(OrdersBuilder ordersBuilder);
}
