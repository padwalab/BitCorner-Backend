package org.sjsu.bitcornerbackend.bills;

import java.util.List;

import org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions.BankAccountNotFoundException;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.InvalidBillDetails;
import org.sjsu.bitcornerbackend.exceptions.billExceptions.SamePayerSenderException;
import org.sjsu.bitcornerbackend.exceptions.userExceptions.UserNotFoundException;
import org.sjsu.bitcornerbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    @Autowired
    private IBillService billService;

    @GetMapping("/all")
    public List<Bill> GetAllBills() {
        return billService.getAllBills();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody BillBuilder billBuilder)
            throws InvalidBillDetails, UserNotFoundException, BankAccountNotFoundException, SamePayerSenderException {
        return ResponseEntity.ok().body(billService.createBill(billBuilder));
    }
}
