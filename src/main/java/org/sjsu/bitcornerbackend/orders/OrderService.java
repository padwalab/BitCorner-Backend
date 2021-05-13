package org.sjsu.bitcornerbackend.orders;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.sjsu.bitcornerbackend.user.UserService;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserService userService;

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

    @Override
    public Set<Orders> all(Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        Set<Orders> userOrders = user.getBankAccount().getOrders();
        return userOrders;
    }

    @Override
    public List<Orders> findByStatus(OrderStatus status) {
        return ordersRepository.findByStatus(status);
    }

}
