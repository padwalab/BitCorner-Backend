package org.sjsu.bitcornerbackend.user;

import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.sjsu.bitcornerbackend.bankAccount.BankAccount;
import org.sjsu.bitcornerbackend.util.Address;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "users_email_unique", columnNames = "email"),
        @UniqueConstraint(name = "users_nickname_unique", columnNames = "nickname") })
public class User {
    @Id
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickName;

    @Embedded
    @JsonProperty("address")
    @AttributeOverrides(value = { @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "state", column = @Column(name = "state")),
            @AttributeOverride(name = "zip", column = @Column(name = "zip")) })
    private Address address;

    @OneToOne(optional = true)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    public User() {
    }

    public User(UserBuilder userBuilder) {
        this.name = userBuilder.name;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.nickName = userBuilder.nickName;
        this.address = userBuilder.address;
        this.bankAccount = userBuilder.bankAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String toString() {
        return "User [address=" + address + ", bankAccount=" + bankAccount + ", email=" + email + ", id=" + id
                + ", name=" + name + ", nickName=" + nickName + ", password=" + password + "]";
    }

}
