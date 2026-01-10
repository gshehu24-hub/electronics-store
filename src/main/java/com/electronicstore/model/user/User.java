package com.electronicstore.model.user;

import java.time.LocalDate;
import java.util.List;

public abstract class User {
    protected String userId;
    protected String username;
    protected String password;
    protected String name;
    protected LocalDate dateOfBirth;
    protected String phoneNumber;
    protected String email;
    protected double salary;
    protected AccessLevel accessLevel;

    public User() {}

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public boolean login() {
        
        return true;
    }

    public void logout() {
        
    }

    public abstract String getRole();

    public List<String> getPermissions() {
        return List.of();
    }

    
}
