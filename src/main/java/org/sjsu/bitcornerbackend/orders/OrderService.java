package org.sjsu.bitcornerbackend.orders;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Orders createOrder(OrdersBuilder ordersBuilder) {
        Orders order = ordersBuilder.build();
        Orders orderResult = ordersRepository.save(order);
        return orderResult;
    }

    @Override
    public List<Orders> all() {
        List<Orders> orders = ordersRepository.findAll();
        return orders;
    }

}
