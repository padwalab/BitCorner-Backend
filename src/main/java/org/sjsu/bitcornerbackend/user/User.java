package org.sjsu.bitcornerbackend.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "users_email_unique", columnNames = "email"),
        @UniqueConstraint(name = "users_nickname_unique", columnNames = "nickname") })
public class User {

    @Id
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @Column(name = "id", updatable = false)
    private long id;

    @NotNull(message = "Name can not be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @NotNull(message = "Password can not be empty")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "Users must select a unique nickname")
    @Column(name = "nickname", nullable = false)
    private String nickName;

    public User() {
    }

    public User(UserBuilder userBuilder) {
        this.name = userBuilder.name;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.nickName = userBuilder.nickName;
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

    @Override
    public String toString() {
        return "User [email=" + email + ", id=" + id + ", name=" + name + ", nickName=" + nickName + ", password="
                + password + "]";
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private String password;
        private String nickName;

        public UserBuilder() {

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

        @Override
        public String toString() {
            return "UserBuilder [email=" + email + ", name=" + name + ", nickName=" + nickName + ", password="
                    + password + "]";
        }

        public User build() {
            this.toString();
            return new User(this);
        }
    }
}
