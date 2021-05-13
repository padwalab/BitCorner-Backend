package org.sjsu.bitcornerbackend.orders;

import java.util.List;
import java.util.Set;

import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByStatus(OrderStatus status);

}
