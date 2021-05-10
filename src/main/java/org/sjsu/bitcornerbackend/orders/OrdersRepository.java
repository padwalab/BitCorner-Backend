package org.sjsu.bitcornerbackend.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
