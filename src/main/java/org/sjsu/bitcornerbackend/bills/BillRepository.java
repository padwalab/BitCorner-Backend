package org.sjsu.bitcornerbackend.bills;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findBySender(Long sender);

    List<Bill> findByPayer(Long payer);
}
