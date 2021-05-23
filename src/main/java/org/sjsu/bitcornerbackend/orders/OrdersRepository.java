package org.sjsu.bitcornerbackend.orders;

import java.util.List;

import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByStatus(OrderStatus status);

    List<Orders> findByStatusAndTypeAndUserNotOrderByCreatedDateAsc(OrderStatus status, OrderType type, Long userId);

    List<Orders> findByStatusAndTypeOrderByCreatedDateAsc(OrderStatus status, OrderType type);

    List<Orders> findByStatusOrderByCreatedDateAsc(OrderStatus status);

    List<Orders> findByStatusAndCurrencyOrderByCreatedDateAsc(OrderStatus status, Currency currency);

}
