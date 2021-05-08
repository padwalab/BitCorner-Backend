package org.sjsu.bitcornerbackend.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.util.Address;

public class UserBuilder {
    @NotNull(message = "Name can not be empty")
    String name;
    @Email(message = "must be a valid email address")
    String email;
    @NotNull(message = "Password can not be empty")
    String password;
    @NotNull(message = "Users must select a unique nickname")
    String nickName;
    public Address address;
    public BankAccount bankAccount;

    public UserBuilder() {
    }

    public UserBuilder setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public UserBuilder setAddress(Address address) {
        this.address = address;
        return this;
    }

    public UserBuilder setUser(User user) {
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setNickName(user.getNickName());
        this.setAddress(user.getAddress());
        this.setBankAccount(user.getBankAccount());
        return this;
    }

    @Override
    public String toString() {
        return "UserBuilder [email=" + email + ", name=" + name + ", nickName=" + nickName + ", password=" + password
                + "]";
    }

    public User build() {
        this.toString();
        return new User(this);
    }
}
