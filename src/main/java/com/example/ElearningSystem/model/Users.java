package com.example.ElearningSystem.model;

import com.example.ElearningSystem.utils.AccountStatus;
import com.example.ElearningSystem.utils.UserRoles;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class Users extends UserPrincipal {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String nic;
    @Column(nullable = false)
    private String userFullName;
    @Column(nullable = false,unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles role;

    private int failedLoginAttempt=0;

@Enumerated(EnumType.ORDINAL)
    private AccountStatus accountStatus;



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getRole().name()));

    }

    public int getFailedLoginAttempt() {
        return failedLoginAttempt;
    }

    public void setFailedLoginAttempt(int failedLoginAttempt) {
        this.failedLoginAttempt = failedLoginAttempt;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", nic='" + nic + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", role=" + role +
                ", failedLoginAttempt=" + failedLoginAttempt +
                ", accountStatus=" + accountStatus.name() +
                '}';
    }
}
