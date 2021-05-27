package org.sjsu.bitcornerbackend.orders;

import java.math.BigDecimal;
import java.util.List;

import org.sjsu.bitcornerbackend.util.Currency;
import org.sjsu.bitcornerbackend.util.OrderStatus;
import org.sjsu.bitcornerbackend.util.OrderType;
import org.sjsu.bitcornerbackend.util.OrderVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

        List<Orders> findByStatus(OrderStatus status);

        List<Orders> findByStatusAndTypeAndUserNotOrderByCreatedDateAsc(OrderStatus status, OrderType type,
                        Long userId);

        List<Orders> findByStatusAndTypeOrderByCreatedDateAsc(OrderStatus status, OrderType type);

        List<Orders> findByVariantAndTypeAndLimitamtGreaterThanEqual(OrderVariant variant, OrderType type,
                        BigDecimal limitamt);

        List<Orders> findByVariantAndTypeAndLimitamtLessThanEqual(OrderVariant variant, OrderType type,
                        BigDecimal limitamt);

        List<Orders> findByStatusOrderByCreatedDateAsc(OrderStatus status);

        List<Orders> findByStatusAndCurrencyOrderByCreatedDateAsc(OrderStatus status, Currency currency);

        List<Orders> findByTypeAndStatusAndCurrencyOrderByLimitamtDescCreatedDateAsc(OrderType type, OrderStatus status,
                        Currency currency);

        List<Orders> findByTypeAndStatusAndCurrencyOrderByLimitamtAscCreatedDateAsc(OrderType type, OrderStatus status,
                        Currency currency);

        List<Orders> findByUserAndTypeOrderByCreatedDateDesc(Long user, OrderType type);

}
