package org.sjsu.bitcornerbackend.bankAccount;

public interface IBankAccountService {
    BankAccount createBankAccount(BankAccountBuilder bankAccountBuilder);
}
