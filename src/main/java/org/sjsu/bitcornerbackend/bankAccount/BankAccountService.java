package org.sjsu.bitcornerbackend.bankAccount;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankAccountService implements IBankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public BankAccount createBankAccount(BankAccountBuilder bankAccountBuilder) {
        BankAccount bankAccount = bankAccountBuilder.build();
        BankAccount bankAccountResult = bankAccountRepository.save(bankAccount);
        return bankAccountResult;
    }

}
